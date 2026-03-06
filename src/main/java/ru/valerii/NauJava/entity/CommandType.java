package ru.valerii.NauJava.entity;

public enum CommandType {
    ADD("add", "добавить транзакцию"),
    LIST("list", "показать все записи"),
    TOTAL("total", "посчитать общую сумму"),
    UPDATE("update", "обновить транзакцию по ID"),
    DELETE("delete", "удалить по ID"),
    EXIT("exit", "выход");

    private final String command;
    private final String description;

    CommandType(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public static CommandType fromString(String text) {
        for (CommandType type : CommandType.values()) {
            if (type.command.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}