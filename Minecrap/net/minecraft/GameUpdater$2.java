/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import java.io.FilePermission;
import java.lang.reflect.Method;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.SecureClassLoader;

class GameUpdater.2
extends URLClassLoader {
    GameUpdater.2(URL[] x0) {
        super(x0);
    }

    protected PermissionCollection getPermissions(CodeSource codesource) {
        PermissionCollection perms = null;
        try {
            Method method = SecureClassLoader.class.getDeclaredMethod("getPermissions", CodeSource.class);
            method.setAccessible(true);
            perms = (PermissionCollection)method.invoke(this.getClass().getClassLoader(), codesource);
            String host = "www.minecraft.net";
            if (host != null && host.length() > 0) {
                perms.add(new SocketPermission(host, "connect,accept"));
            } else {
                codesource.getLocation().getProtocol().equals("file");
            }
            perms.add(new FilePermission("<<ALL FILES>>", "read"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return perms;
    }
}
