package br.com.delogic.asd.repository.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.SmartDataSource;

public class LocalSingleConnectionDataSource implements SmartDataSource {

    private Connection connection;
    private static final UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException(
        "Nao implementado em " + LocalSingleConnectionDataSource.class);
    private boolean shouldClose = false;
    private final DataSource dataSource;

    public LocalSingleConnectionDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean shouldClose(Connection con) {
        return shouldClose;
    }

    public void releaseConnection() {
        this.shouldClose = true;
        DataSourceUtils.releaseConnection(connection, dataSource);
        this.shouldClose = false;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DataSourceUtils.getConnection(dataSource);
        }
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

}
