/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation.
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/**
 * $Id$
 *
 * @author Raja Perumal
 *         07/13/02
 */

package ee.jakarta.tck.authentication.test.common.logging.server;

import static java.util.logging.Level.SEVERE;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * TSLogger is the custom Logger which extends java.util.Logger
 *
 **/
public class TSLogger extends Logger {

    private int levelValue = Level.INFO.intValue();
    private int offValue = Level.OFF.intValue();
    private Filter filter;

    // Note : The logger instance should not be
    // stored in this instance variable,
    // it should be kept at the log Manager using
    //
    // LogManager.addLogger(TSlogger);
    //
    // and it can be retrieved using
    //
    // LogManager.getLogger(name);
    //
    // Since Logger and TSLogger are of different types
    // we cannot use the above logic and hence we have
    // no choice except to store it here.
    //
    private static TSLogger tsLogger = null;
    public static TSLogger logger;

    private static FileHandler fileHandler;

    protected TSLogger(String name) {
        super(name, null);
        levelValue = Level.INFO.intValue();
    }

    public static TSLogger getTSLogger() {
        initializeTSLogger();
        return logger;
    }

    public static void initializeTSLogger() {
        if (logger != null) {
            return;
        }

        try {
            String logFileLocation = System.getProperty("log.file.location");
            if (logFileLocation != null) {
                logger = TSLogger.getTSLogger("jacc");
                boolean appendMode = true;

                String fileName = "/authentication-trace-log.xml";

                // Clean the content of authentication-trace-log.xml if it exists
                File file = new File(logFileLocation + "/authentication-trace-log.xml");
                if (file.exists()) {
                    System.out.println("XXXX:  in initializeTSLogger() - authentication-trace-log.xml exists");
                    // Delete the file, if it exists
                    // file.delete();
                    fileName = "/client-authentication-trace-log.xml";
                }

                File fileLock = new File(logFileLocation + "/authentication-trace-log.xml.lck");
                if (fileLock.exists()) {
                    System.out.println("XXXX:  in initializeTSLogger() - authentication-trace-log.xml.lck exists");
                    // Delete the file, if it exists
                    // fileLock.delete();
                }

                // Create a new file
                System.out.println("XXXX:  in initializeTSLogger() - about to create authentication-trace-log.xml");
                fileHandler = new FileHandler(logFileLocation + fileName, appendMode);
                fileHandler.setFormatter(new TSXMLFormatter());
                logger.addHandler(fileHandler);
                setTSLogger(logger);
            } else {
                // use default logging mechanism
                logger = TSLogger.getTSLogger("jacc");
                setTSLogger(logger);
                logger.log(SEVERE, "log.file.location not set: Using default logger");
            }
        } catch (Exception e) {
            throw new RuntimeException("TSLogger Initialization failed", e);
        }
    }

    public static void setTSLogger(TSLogger lgr) {
        logger = lgr;
    }

    public static void close() {
        // fileHandler.close();
    }


    /**
     * Find or create a logger for a named subsystem. If a logger has already been created with the given name it is
     * returned. Otherwise a new logger is created.
     * <p>
     * If a new logger is created its log level will be configured based on the LogManager configuration and it will
     * configured to also send logging output to its parent's handlers. It will be registered in the LogManager global
     * namespace.
     *
     * @param name A name for the logger. This should be a dot-separated name and should normally be based on the package
     * name or class name of the subsystem, such as java.net or javax.swing
     * @return a suitable Logger
     */
    public static synchronized TSLogger getTSLogger(String name) {
        TSLogger result = null;

        LogManager manager = LogManager.getLogManager();

        if (tsLogger != null) {
            if (tsLogger.getName().equals(name)) {
                result = tsLogger;
            }
        } else {
            result = new TSLogger(name);
            manager.addLogger(result);
        }

        return result;
    }

    /**
     * Log a message, with no arguments.
     * <p>
     * If the logger is currently enabled for the given message level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     *
     * @param level One of the message level identifiers, e.g. SEVERE
     * @param msg The string message (or a key in the message catalog)
     */
    @Override
    public void log(Level level, String msg) {
        // assign default context (authentication_ctx) to all messages ???
        log(level, msg, "authentication_ctx");
    }

    /**
     * Log a message, with no arguments.
     * <p>
     * If the logger is currently enabled for the given message level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     *
     * @param level One of the message level identifiers, e.g. SEVERE
     * @param msg The string message (or a key in the message catalog)
     * @param contextId the logging context Id
     */
    public void log(Level level, String msg, String contextId) {
        if (level.intValue() < levelValue || levelValue == offValue) {
            return;
        }

        TSLogRecord lr = new TSLogRecord(level, msg, contextId);
        String rbn = null;

        Logger target = this;
        while (target != null) {
            rbn = target.getResourceBundleName();
            if (rbn != null) {
                break;
            }
            target = target.getParent();
        }

        if (rbn != null) {
            lr.setResourceBundleName(rbn);
        }

        log(lr);
    }

    /**
     * Log a TSLogRecord.
     *
     * @param record the TSLogRecord to be published
     */
    public void log(TSLogRecord record) {
        if (record.getLevel().intValue() < levelValue || levelValue == offValue) {
            return;
        }

        synchronized (this) {
            if (filter != null && !filter.isLoggable(record)) {
                return;
            }
        }

        // Post the LogRecord to all our Handlers, and then to
        // our parents' handlers, all the way up the tree.

        TSLogger logger = this;
        while (logger != null) {
            Handler targets[] = logger.getHandlers();

            if (targets != null) {
                for (int i = 0; i < targets.length; i++) {
                    // targets[i].publish(record);

                    // Publish record only if the
                    // handler is of type FileHandler
                    // Do not publish to all parent handler
                    // Parent handler may not be able to
                    // Format the TSLogRecord, because
                    // TSLogRecord is the custom record.
                    if (targets[i] instanceof FileHandler) {
                        targets[i].publish(record);
                    }
                }
            }

            if (!logger.getUseParentHandlers()) {
                break;
            }

            logger = null;
        }
    }

}
