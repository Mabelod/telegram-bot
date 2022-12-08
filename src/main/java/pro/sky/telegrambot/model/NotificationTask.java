package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
@Entity
public class NotificationTask {
    @Id
    @GeneratedValue
    private Long id;
    private Long idChat;
    private String notification;
    private String login;
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfDispatch;

    public NotificationTask(Long id, Long idChat, String notification, String login, String firstName, String lastName, LocalDateTime dataOfDispatch) {
        this.id = id;
        this.idChat = idChat;
        this.notification = notification;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfDispatch = dataOfDispatch;
    }

    public NotificationTask() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getDateOfDispatch() {
        return dateOfDispatch;
    }

    public void setDateOfDispatch(LocalDateTime dateOfDispatch) {
        this.dateOfDispatch = dateOfDispatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(idChat, that.idChat) && Objects.equals(notification, that.notification) && Objects.equals(login, that.login) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(dateOfDispatch, that.dateOfDispatch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idChat, notification, login, firstName, lastName, dateOfDispatch);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", idChat=" + idChat +
                ", notification='" + notification + '\'' +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dataOfDispatch=" + dateOfDispatch +
                '}';
    }
}
