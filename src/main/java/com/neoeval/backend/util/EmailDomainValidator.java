package com.neoeval.backend.util;

import java.util.Hashtable;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class EmailDomainValidator {

    /**
     * Checks if a given domain has Mail Exchange (MX) records.
     *
     * @param domain The domain name to check (e.g., "gmail.com").
     * @return true if the domain has at least one MX record, false otherwise.
     */
    public static boolean hasMXRecord(String domain) {
        if (domain == null || domain.trim().isEmpty()) {
            return false;
        }

        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

            DirContext ictx = new InitialDirContext(env);
            Attributes attrs = ictx.getAttributes(domain, new String[]{"MX"});
            Attribute attr = attrs.get("MX");

            // If an MX attribute was found, the domain can receive emails.
            if (attr != null && attr.size() > 0) {
                return true;
            }

            // Fallback: Sometimes domains only have an A record instead of an MX record
            // but still receive emails (though uncommon for major providers).
            // Let's strictly require MX records to avoid fake domains.
            return false;

        } catch (NamingException e) {
            // A NamingException means the domain does not exist or DNS lookup failed
            return false;
        }
    }
}
