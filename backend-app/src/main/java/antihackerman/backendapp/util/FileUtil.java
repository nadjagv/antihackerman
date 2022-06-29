package antihackerman.backendapp.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtil {

    public static String readFile(MultipartFile file) throws Exception {
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
            is.close();
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
        return sb.toString();
    }

    public static String readFile(String path) throws Exception {
        BufferedReader br;

        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            String line;
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
            is.close();
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
        return sb.toString();
    }

    public static String readFile(File file) throws Exception {
        BufferedReader br;

        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = new FileInputStream(file);
            String line;
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
            is.close();
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
        return sb.toString();
    }

}
