package hi.im.jenga.board.util;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Date;

public class CustomDateHandler extends BaseTypeHandler<Date> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public Date getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Timestamp sqlTimestamp = resultSet.getTimestamp(s);
        if (sqlTimestamp != null) {
            System.out.println(new Date(sqlTimestamp.getTime()));
            return new Date(sqlTimestamp.getTime());
        }
        return null;
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public Date getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
