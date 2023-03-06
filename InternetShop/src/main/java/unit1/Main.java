package unit1;

import java.util.logging.*;
import java.util.Date;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());
    static final String LOG_FILE = "log.txt";
    static final String CHARSET_FILE = "UTF-8";
    static final String CHARSET_CONSOLE = "UTF-8";//"CP866";

    public static void main(String[] args) throws UserException {
        // Инициализация логера
        loggerInit(LOG_FILE);

        Category categoryBooks = new Category("Книги");
        Product book1 = new Product("Краткая история человечества", 200.50F, 9, 4);
        Product book2 = new Product("Математические олимпиады в стране сказок", 500.10F, 7, 6);
        Category categoryFood = new Category("Еда");
        Product food1 = new Product("Кефир", 45.15F, 5, 14);
        Product food2 = new Product("Финики", 19.85F, 3, 16);
        // заполнить категрии товаром
        try {
            categoryBooks.addProduct(book1);
            categoryBooks.addProduct(book2);
            categoryFood.addProduct(food1);
            categoryFood.addProduct(food2);
        } catch (ShopException se) {
            logger.warning(se.getMessage());
        }

        // Вывод в консоль каталогов товаров
        logger.warning(categoryBooks.toString());
        logger.warning(categoryFood.toString());

        // создать пользователей
        User user1 = null;
        User user2 = null;
        try {
            user1 = new User("Гена Крокодил", "***");
            user2 = new User("Чебурашка", "******");
        } catch (ShopException se) {
            logger.warning(se.getMessage());
        }

        // заполнить корзину товарами
        user1.addProduct(book1, 1);
        user1.addProduct(food1, 1);
        user2.addProduct(book2, 2);
        user2.addProduct(food1, 2);

        // запросим больше, чем остаток
        if(!user1.addProduct(food1, 20)) {
            // запросим меньше, чем остаток
            user1.addProduct(food1, 5);
        }

        // отобразить корзины покупателей
        logger.warning("");
        logger.warning(user1.toString());
        logger.warning("");
        logger.warning(user2.toString());

        // произвести покупку
        user1.sale();
        user2.sale();

        // Вывод в консоль каталогов товаров
        logger.warning("");
        logger.warning(categoryBooks.toString());
        logger.warning(categoryFood.toString());
    }

    // Инициализация логера
    public static void loggerInit(String aFileName) {
        FileHandler fh = null;
        try {
            fh = new FileHandler(aFileName, true);
        } catch (Exception e) {
            System.out.println("Проблемы с файлом "+aFileName+" "+e.getMessage());
        }
        if(fh == null) System.exit(0);
        try {
            fh.setEncoding(CHARSET_FILE);
        } catch (Exception e) {
            System.out.println("Проблемы с кодировкой FileHandler "+e.getMessage());
        }
        fh.setLevel(Level.INFO); // все что ниже INFO не работает, зараза.
        //fh.setLevel(Level.FINE);
        logger.addHandler(fh);

//        SimpleFormatter sFormat = new SimpleFormatter();
//        fh.setFormatter(sFormat);
        fh.setFormatter(withoutRipplesInTheEyesFormatter);

        // Изменение консольного логера, которые уже создан по умолчанию
        for (Handler h : LogManager.getLogManager().getLogger("").getHandlers()) {
            if (h instanceof ConsoleHandler) {
                if (h.getFormatter() == null || !(h.getFormatter() instanceof EmptyFormatter)) {
                    h.setFormatter(emptyFormatter);
                    try {
                        h.setEncoding(CHARSET_CONSOLE);
                        h.setLevel(Level.WARNING); // все что ниже INFO не работает, зараза.
                        //h.setLevel(Level.INFO);
                    } catch (Exception e) {
                        System.out.println("Проблемы с кодировкой ConsoleHandler "+e.getMessage());
                    }
                    //break;
                }
            }
        }
/*      // он там по умолчанию создан
        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(sFormat);
        try {
            ch.setEncoding(CHARSET_CONSOLE);
        } catch (Exception e) {
           System.out.println("Проблемы с кодировкой ConsoleHandler "+e.getMessage());
        }
        logger.addHandler(ch);
*/
        logger.info("\t\t\n\n------------------------------------------------------------\n");
        logger.info("\t\tЛогирование инициализировано");
    }

    // Создание пустого формата для консоли
    static class EmptyFormatter extends Formatter {
        String lineSeparator = System.getProperty("line.separator");
        @Override public synchronized String format(LogRecord record) {
            return formatMessage(record)+lineSeparator;
        }
    }
    static EmptyFormatter emptyFormatter = new EmptyFormatter();

    static final Date dat = new Date();

    // Создание формата для файла лога, чтобы не рябило в глазах.
    static class WithoutRipplesInTheEyesFormatter extends Formatter {
        String lineSeparator = System.getProperty("line.separator");
        String yyyy_MM_dd_HH_mm_ss = "yyyy.MM.dd HH:mm:ss";
        @Override public synchronized String format(LogRecord record) {
            dat.setTime(record.getMillis());
            String dateStr = (new SimpleDateFormat( yyyy_MM_dd_HH_mm_ss )).format(dat);
            String message = formatMessage(record);
            String throwable = "";
            if (record.getThrown() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.print("");
                record.getThrown().printStackTrace(pw);
                pw.close();
                throwable = sw.toString();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(dateStr)
                    .append(" ")
                    .append(((record.getSourceClassName() != null) ? record.getSourceClassName() : record.getLoggerName()))
                    .append(" ")
                    .append(((record.getSourceMethodName() != null) ? record.getSourceMethodName() : ""))
                    .append("\t")
                    .append(record.getLevel().getName())
                    .append("  ")
                    .append(message)
                    .append(throwable)
                    .append(lineSeparator);
            return sb.toString();
        }
    }
    static WithoutRipplesInTheEyesFormatter withoutRipplesInTheEyesFormatter
            = new WithoutRipplesInTheEyesFormatter();

}