package com.thinkerwolf.hantis.common;

public class DefaultNameHandler implements NameHandler {

    @Override
    public String convertToPropertyName(String columnName) {
        StringBuilder propertyName = new StringBuilder(columnName.length());
        boolean b = false;
        for (int i = 0; i < columnName.length(); i++) {
            char c = columnName.charAt(i);
            switch (c) {
                case '_':
                    b = true;
                    break;
                default:
                    if (b) {
                        propertyName.append(Character.toUpperCase(c));
                        b = false;
                    } else {
                        propertyName.append(c);
                    }
                    break;
            }
        }
        return propertyName.toString();
    }

    @Override
    public String convertToColumnName(String propertyName) {
        StringBuilder columnName = new StringBuilder(propertyName.length());
        for (int i = 0, len = propertyName.length(); i < len; i++) {
            char c = propertyName.charAt(i);
            boolean add = false;
            if (c <= 'Z' && c >= 'A') {
                add = true;
            }
            if (add && i != 0) {
                columnName.append('_');
                columnName.append(Character.toLowerCase(c));
            } else {
                columnName.append(Character.toLowerCase(c));
            }
        }
        return columnName.toString();
    }

    @Override
    public String name() {
        return "default";
    }

}
