import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // Создание потоков скачивания
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите URL Музыки");
        String URLMusic = scanner.nextLine();
        System.out.println("Введите URL Картинки");
        String URLPicture = scanner.nextLine();
        Thread MP3 = new Thread(() -> DownloadWithUrl(URLMusic, "file.mp3"));
        Thread Picture = new Thread(() -> DownloadWithUrl(URLPicture, "picture.jpg"));
       MP3.start();
        Picture.start();
    }

    public static void DownloadWithUrl(String URL, String filePath){ // Метод для загрузки
        try {
            URLConnection connection = new URL(URL).openConnection(); // Подключаемся к URL
            InputStream is = connection.getInputStream(); // Получаем входящий поток данных из соединения
            OutputStream os = new FileOutputStream(filePath);// Создаем выходной поток для записи файла на диск

            os.write(is.readAllBytes()); // Читаем все байты из входящего потока и записываем их в выходной поток
            os.close();
            is.close();
            Desktop.getDesktop().open(new File("file.mp3")); // Последовательный запуск файлов
            Desktop.getDesktop().open(new File("picture.jpg"));
            String PictureType = getFileType("picture.jpg");
            if (Objects.equals(PictureType, "JPEG")){
                System.out.println("Тип файла совпадает");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String getFileType(String filename) {
        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] magicBytes = new byte[4];
            if (fis.read(magicBytes) != -1) {
                return determineFileType(magicBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String determineFileType(byte[] magicBytes) {
        if (magicBytes.length < 4) {
            return null;
        }

        // Например, определим MP3, PNG, JPEG и PDF по их магическим числам
        if (magicBytes[0] == (byte) 0x49 && magicBytes[1] == (byte) 0x44 &&
                magicBytes[2] == (byte) 0x33) {
            return "MP3";
        } else if (magicBytes[0] == (byte) 0x89 && magicBytes[1] == (byte) 0x50 &&
                magicBytes[2] == (byte) 0x4E && magicBytes[3] == (byte) 0x47) {
            return "PNG";
        } else if (magicBytes[0] == (byte) 0xFF && magicBytes[1] == (byte) 0xD8) {
            return "JPEG";
        } else if (magicBytes[0] == (byte) 0x25 && magicBytes[1] == (byte) 0x50 &&
                magicBytes[2] == (byte) 0x44 && magicBytes[3] == (byte) 0x46) {
            return "PDF";
        }

        return "Неопределенный тип";
    }
}