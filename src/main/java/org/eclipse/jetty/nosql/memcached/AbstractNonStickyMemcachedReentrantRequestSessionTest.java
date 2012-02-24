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

import org.eclipse.jetty.server.session.AbstractTestServer;
import org.junit.Test;

/**
 * ReentrantRequestSessionTest
 */
public abstract class AbstractNonStickyMemcachedReentrantRequestSessionTest extends AbstractMemcachedReentrantRequestSessionTest
{
    public AbstractTestServer createServer(int port)
    {
        return new NonStickyMemcachedTestServer(port);
    }

    @Test
    public void testReentrantRequestSession() throws Exception
    {
        // FIXME: non-sticky cannot pass this test by design
    }
}
