package com.digitechlabs.paymentgw.soap;

import com.digitechlabs.paymentgw.configs.MyLog;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * This class accepts client connection on given port. When the connection is
 * accepted, the listener creates an instance of <code>ServerSession</code>,
 * generates new <code>PDUProcessor</code> using object derived from
 * <code>PDUProcessorFactory</code>, passes the processor to the smsc session
 * and starts the session as a standalone thread.
 */
public class HttpListener implements Runnable {

    private ServerSocket serverSockets;
    private long acceptTimeout = 10;
    private int mPort;
    private boolean isReceiving;
    protected RequestQueue mQueue;

    /**
     * Constructor with control if the listener starts as a separate thread. If
     * <code>asynchronous</code> is true, then the listener is started as a
     * separate thread, i.e. the creating thread can continue after calling of
     * method <code>enable</code>. If it's false, then the caller blocks while
     * the listener does it's work, i.e. listening.
     *
     * @param port list of listener port
     * @param reqQueue
     * @param resQueue
     * @param logMdl index of log module
     * @param authen the authenticator object
     * @see #start()
     */
    public HttpListener(int port) {
        mPort = port;
        mQueue = new RequestQueue();
    }

    /**
     * Starts the listening. If the listener is asynchronous (recommended), then
     * new thread is created which listens on the port and the
     * <code>enable</code> method returns to the caller. Otherwise the caller is
     * blocked in the enable method.
     *
     * @see #start()
     */
    public void start() {
        if (!isReceiving) {
            instance(mPort);
            isReceiving = true;
            Thread t = new Thread(this);
            t.start();
        }
    }

    /**
     * Signals the listener that it should disable listening and wait until the
     * listener stops. Note that based on the timeout settings it can take some
     * time before this method is finished -- the listener can be blocked on i/o
     * operation and only after exiting i/o it can detect that it should
     * disable.
     *
     * @see #start()
     */
    public void stop() {
        try {
            if (serverSockets != null) {
                serverSockets.close();
            }
        } catch (IOException e) {
            Logger.getLogger(HttpListener.class).error(e.getMessage(), e);
        }

        isReceiving = false;
    }

    /**
     * The actual listening code which is run either from the thread (for async
     * listener) or called from <code>enable</code> method (for sync listener).
     * The method can be exited by calling of method <code>disable</code>.
     *
     * @see #start()
     * @see #stop()
     */
    @Override
    public void run() {
        while (isReceiving) {
            try {
                listen();
                Thread.sleep(1);
            } catch (Exception e) {
                MyLog.Error(e);
            }
        }
    }

    /**
     * The "one" listen attempt called from <code>run</code> method. The
     * listening is atomised to allow control stopping of the listening. The
     * length of the single listen attempt is defined by
     * <code>acceptTimeout</code>. If a connection is accepted, then new session
     * is created on this connection, new PDU processor is generated using PDU
     * processor factory and the new session is started in separate thread.
     *
     * @see #run()
     */
    private void listen() {
        try {
            if (serverSockets == null || serverSockets.isClosed()) {
                MyLog.Error("Listener on port " + mPort + " failed, retry to bind ...");
                Thread.sleep(1);
                if (serverSockets != null) {
                    serverSockets.close();
                }

                instance(mPort);
            }

            Socket s = serverSockets.accept();
            if (s != null) {
                mQueue.addToQueue(s);
            }
            //            mQueue.addToQueue(serverSockets.accept());
        } catch (java.net.SocketTimeoutException e) {
            Logger.getLogger(HttpListener.class).debug(e.getMessage(), e);
        } catch (InterruptedException | IOException e) {
            Logger.getLogger(HttpListener.class).error(e.getMessage(), e);
        }
    }

    /**
     * Sets new timeout for accepting new connection. The listening blocks the
     * for maximum this time, then it exits regardless the connection was
     * accepted or not.
     *
     * @param value the new value for accept timeout
     */
    public void setAcceptTimeout(int value) {
        acceptTimeout = value;
    }

    /**
     * Returns the current setting of accept timeout.
     *
     * @return the current accept timeout
     * @see #setAcceptTimeout(int)
     */
    public long getAcceptTimeout() {
        return acceptTimeout;
    }

    private ServerSocket createSocket(int port) throws IOException {
        ServerSocket socket = null;
        try {
            MyLog.Infor("Binding to Port " + port);
            socket = new ServerSocket(port);
            socket.setSoTimeout((int) getAcceptTimeout());
            return socket;
        } catch (Exception ex) {
            Logger.getLogger(HttpListener.class).error("Can't not bind Port:" + port + " --> " + ex.getMessage(), ex);
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    java.util.logging.Logger.getLogger(HttpListener.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            return null;
        }
    }

    protected void instance(int port) {
        MyLog.Infor("Create instance soap listener");
        try {
            serverSockets = createSocket(port);
        } catch (IOException e) {
            MyLog.Error("Binding to Port " + port + " error, port has already used");
            MyLog.Error(e);
            if (serverSockets != null) {
                try {
                    serverSockets.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(HttpListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.exit(1);
        }
    }
}
