package app.web.pavelk.mouth1.domain;

public final class Views { // интерфейс для фильтра выдачи апи
    public interface Id {
    }

    public interface IdName extends Id {
    }

    public interface FullComment extends IdName {
    }

    public interface FullMessage extends IdName {
    }

    public interface FullProfile extends IdName {
    }
}
