package org.eclipse.jetty.nosql.memcached;

// ========================================================================
// Copyright (c) 1996-2009 Mort Bay Consulting Pty. Ltd.
// ------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// and Apache License v2.0 which accompanies this distribution.
// The Eclipse Public License is available at 
// http://www.eclipse.org/legal/epl-v10.html
// The Apache License v2.0 is available at
// http://www.opensource.org/licenses/apache2.0.php
// You may elect to redistribute this code under either of these licenses. 
// ========================================================================



import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.nosql.kvs.KeyValueStoreSessionIdManager;
import org.eclipse.jetty.nosql.memcached.MemcachedSessionIdManager;
import org.eclipse.jetty.nosql.memcached.MemcachedSessionManager;
import org.eclipse.jetty.nosql.memcached.hashmap.HashMapClientFactory;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.session.AbstractTestServer;
import org.eclipse.jetty.server.session.SessionHandler;


/**
 * @version $Revision$ $Date$
 */
public class MemcachedTestServer extends AbstractTestServer
{
    protected KeyValueStoreSessionIdManager _idManager;
    protected boolean _saveAllAttributes = false; // false save dirty, true save all
    
    public MemcachedTestServer(int port)
    {
        this(port, 30, 10);
    }

    public MemcachedTestServer(int port, int maxInactivePeriod, int scavengePeriod)
    {
        this(port, maxInactivePeriod, scavengePeriod, null);
    }
    
    public MemcachedTestServer(int port, int maxInactivePeriod, int scavengePeriod, String sessionIdMgrConfig) {
        super(port, maxInactivePeriod, scavengePeriod, sessionIdMgrConfig);
    }
    
    public MemcachedTestServer(int port, int maxInactivePeriod, int scavengePeriod, boolean saveAllAttributes)
    {
        this(port, maxInactivePeriod, scavengePeriod);
        
        _saveAllAttributes = saveAllAttributes;
    }

    public SessionIdManager newSessionIdManager(String config) // TODO: use config as serverString
    {
        if (config == null) {
            config = "127.0.0.1:11211";
        }
        if ( _idManager != null )
        {
            try
            {
                _idManager.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            _idManager.setScavengePeriod((int) TimeUnit.SECONDS.toMillis(_scavengePeriod));
            _idManager.setWorkerName("node0");
            
            try
            {
                _idManager.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            return _idManager;
        }
        
        try
        {
            System.err.println("MemcachedTestServer:SessionIdManager:" + _maxInactivePeriod + "/" + _scavengePeriod);
            // FIXME: need more sophisticated way to change this behavior
            String useMock = System.getProperty("org.eclipse.jetty.nosql.memcached.useMock", "true");
            if ("false".equals(useMock)) {
                _idManager = new MemcachedSessionIdManager(_server, config);
            } else {
                _idManager = new MemcachedSessionIdManager(_server, config, new HashMapClientFactory());
            }
            _idManager.setScavengePeriod((int)TimeUnit.SECONDS.toMillis(_scavengePeriod));
            _idManager.setKeyPrefix("MemcachedTestServer::");
            
            return _idManager;
        }
        catch (Exception e)
        {
            throw new IllegalStateException();
        }
    }

    public SessionManager newSessionManager()
    {
        MemcachedSessionManager manager;
        try
        {
            manager = new MemcachedSessionManager();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        
        manager.setSavePeriod(1);
        manager.setStalePeriod(0);
        manager.setSaveAllAttributes(_saveAllAttributes);
        //manager.setScavengePeriod((int)TimeUnit.SECONDS.toMillis(_scavengePeriod));
        return manager;
    }

    public SessionHandler newSessionHandler(SessionManager sessionManager)
    {
        return new SessionHandler(sessionManager);
    }
    
    public static void main(String... args) throws Exception
    {
        MemcachedTestServer server8080 = new MemcachedTestServer(8080);
        server8080.addContext("/").addServlet(SessionDump.class,"/");
        server8080.start();
        
        MemcachedTestServer server8081 = new MemcachedTestServer(8081);
        server8081.addContext("/").addServlet(SessionDump.class,"/");
        server8081.start();
        
        server8080.join();
        server8081.join();
    }

}
