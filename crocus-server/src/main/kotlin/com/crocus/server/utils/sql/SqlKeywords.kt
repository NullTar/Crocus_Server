package com.crocus.server.utils.sql

object SqlKeywords {
    val keywordsSet = setOf(
        "INSERT", "UPDATE", "DELETE", "TRUNCATE", "DROP", "ALTER", "CREATE", "REPLACE", "MERGE",
        "DATABASE", "TABLE", "INDEX", "SCHEMA", "VIEW", "GRANT", "REVOKE", "DENY", "EXECUTE",
        "CALL", "COMMIT", "ROLLBACK", "SAVEPOINT", "RELEASE SAVEPOINT", "USER", "ROLE", "SET PASSWORD",
        "LOAD DATA", "LOAD FILE", "INFILE", "OUTFILE", "RENAME", "REPAIR", "OPTIMIZE", "ANALYZE",
        "BACKUP", "RESTORE", "LOCK", "UNLOCK", "SELECT", "FROM", "WHERE", "JOIN", "UNION", "ALL",
        "EXCEPT", "INTERSECT", "NULL", "IS", "AND", "OR", "NOT", "LIKE", "IN", "BETWEEN", "EXISTS",
        "HAVING", "CASE", "WHEN", "THEN", "END", "CAST", "CONVERT", "EXEC", "DECLARE", "true",
        "TRUE", "xp_cmdshell", "sysobjects", "syscolumns", "information_schema"
    )
}