package org.errorzhu.zsql.core.result;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public class ResultWrapper {

    private final ResultSet rs;

    public ResultWrapper(ResultSet resultSet) {
        this.rs = resultSet;
    }

    private void fillWithDisplaySize(int[] type, int[] colCounts) {
        for (int i = 0; i < type.length; i++) {
            switch (type[i]) {
                case Types.BOOLEAN:
                case Types.TINYINT:
                case Types.SMALLINT:
                    colCounts[i] = 4;
                    break;
                case Types.INTEGER:
                case Types.BIGINT:
                case Types.REAL:
                case Types.FLOAT:
                case Types.DOUBLE:
                    colCounts[i] = 8;
                    break;
                case Types.CHAR:
                case Types.VARCHAR:
                    colCounts[i] = 20;
                    break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    colCounts[i] = 20;
                    break;
                default:
                    colCounts[i] = 20;
            }
        }
    }

    public void print() throws SQLException {
        ResultSetMetaData meta = this.rs.getMetaData();
        int length = meta.getColumnCount();
        String[] colLabels = new String[length];
        int[] colCounts = new int[length];
        int[] types = new int[length];
        int[] changes = new int[length];
        Arrays.fill(changes, 0);

        for (int i = 0; i < meta.getColumnCount(); i++) {
            colLabels[i] = meta.getColumnLabel(i + 1).toUpperCase();
            types[i] = meta.getColumnType(i + 1);
        }

        fillWithDisplaySize(types, colCounts);

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < meta.getColumnCount(); i++) {
            if (colLabels[i].length() > colCounts[i]) {
                changes[i] = colLabels[i].length() - colCounts[i];
                colCounts[i] = colLabels[i].length();
            }
            int sep = (colCounts[i] - colLabels[i].length());
            builder.append(String.format("|%s%" + (sep == 0 ? "" : sep) + "s", colLabels[i], ""));
        }
        builder.append("|");
        int[] colWeights = Arrays.copyOf(colCounts, colCounts.length);

        Function<String[], int[]> component = (labels) -> {
            int[] weights = new int[colWeights.length];
            for (int i = 0; i < weights.length; i++) {
                weights[i] = colWeights[i] + changes[i];
            }
            return weights;
        };

        Supplier<String> framer = () ->
                "+" + Arrays.stream(component.apply(colLabels))
                        .mapToObj(col -> {
                            char[] fr = new char[col];
                            Arrays.fill(fr, '-');
                            return new String(fr);
                        }).reduce((x, y) -> x + "+" + y).orElse("") + "+";

        if (!this.rs.next()) {
            System.out.println("[Empty set]");
            return;
        }

        System.out.println(framer.get());
        System.out.println(builder.toString());
        System.out.println(framer.get());

        do {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < meta.getColumnCount(); i++) {
                String value = this.rs.getString(i + 1);
                if (value == null) {
                    value = "null";
                }
                if (value.length() > colCounts[i]) {
                    changes[i] = value.length() - colCounts[i];
                    colCounts[i] = value.length();
                }
                int sep = (colCounts[i] - value.length());
                line.append(
                        String.format("|%s%" + (sep == 0 ? "" : sep) + "s", value, ""));
            }
            line.append("|");
            System.out.println(line.toString());
        }
        while (this.rs.next());

        System.out.println(framer.get());


    }

}
