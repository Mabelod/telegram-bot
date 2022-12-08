package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private NotificationTaskRepository notificationTaskRepository;
    private long chatId;
    private String receivedMessage;
    private String username;
    private String firstName;
    private String lastName;


    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }
    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {

            logger.info("Processing update: {}", update);
            chatId = update.message().chat().id();
            receivedMessage = update.message().text();
            username = update.message().chat().username();
            firstName = update.message().chat().firstName();
            lastName = update.message().chat().lastName();

            if (receivedMessage.equals("/start")) {
                mailing(chatId,
                        "Данный бот создает напоминание на выбранную дату." +
                        "Введите сообщение в формате дата + напоминание." +
                        "Например: 01.01.2022 20:00 Сделать домашнюю работу");
            } else if (receivedMessage.matches("([0-9.:\\s]{16})(\\s)([\\W+]+)") ) {
                parsing(receivedMessage);
            } else {
                mailing(chatId, "Введите данные согластно примеру.");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    @Scheduled(cron = "0 0/1 * * * *")
    public void start() {
        logger.info("Проверка напоминания");
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> notify = notificationTaskRepository.findByDateOfDispatch(today);
        for (NotificationTask notificationTask : notify) {
            mailing(notificationTask.getIdChat(), notificationTask.getNotification());
        }
    }
    public void mailing(long chatId, String receivedMessage) {
        logger.info("Отправка сообщения");
        SendMessage message = new SendMessage(chatId, receivedMessage);
        SendResponse response = telegramBot.execute(message);
    }

    private void parsing(String receivedMessage) {
        logger.info("Парсинг");
        Pattern pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)");
        Matcher matcher = pattern.matcher(receivedMessage);
        if (matcher.matches()) {
            String date = matcher.group(1);
            String item = matcher.group(3);
            try {
                LocalDateTime yearOfNotification = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                LocalDateTime today = LocalDateTime.now();
                int yearOfNotification1 = yearOfNotification.getYear();
                int today1 = today.getYear();
                if (yearOfNotification1 < today1) {
                    mailing(chatId, "Введите дату предстоящего события");
                } else {
                    NotificationTask memo = new NotificationTask(1L,
                            chatId,
                            item,
                            username,
                            firstName,
                            lastName,
                            yearOfNotification);
                    notificationTaskRepository.save(memo);
                    mailing(chatId, "Напоминание установлено");
                }
            } catch (Exception e) {
                mailing(chatId, "Введите корректную дату в формате : dd.MM.yyyy HH:mm");
            }
        }
    }

}
