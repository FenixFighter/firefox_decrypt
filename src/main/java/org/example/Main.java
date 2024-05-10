package org.example;

import java.io.*;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final String SYSTEM = System.getProperty("os.name").split(" ")[0].toUpperCase();
    private static final boolean sys64 = Long.MAX_VALUE > Math.pow(2, 32);
    private static final String userHome = System.getProperty("user.home");

    public static void main(String[] args) {
        System.out.println(get_version());
    }


    private static String getInternalVersion() {
        int[] versionInfo = {1, 1, 1};  // Внутренняя версия
        StringBuilder version = new StringBuilder();
        for (int i = 0; i < versionInfo.length; i++) {
            version.append(versionInfo[i]);
            if (i < versionInfo.length - 1) {
                version.append(".");
            }
        }
        return version.toString();
    }

    private static String get_version(){
        ProcessBuilder processBuilder = new ProcessBuilder("git", "describe", "--tags");
        try {
            Process process = processBuilder.start();

            // Чтение вывода процесса
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            // Получение версии
            String version = bufferedReader.readLine() ==null ? "": bufferedReader.readLine();
            if (version.isEmpty()) {
                return getInternalVersion();
            } else {
                process.waitFor();  // Дождаться завершения процесса (не обязательно)
                return version;
            }
        } catch (IOException | InterruptedException e) {
            return getInternalVersion();
        }
    }

    private static void load_libnss(){
        String nssLibPath = System.getProperty("NSS_LIB_PATH", "");
        List<String> locations = new ArrayList<>();
        locations.add(nssLibPath);

        if(SYSTEM == "WINDOWS"){
            String nssname = "nss3.dll";
            if(sys64 != true){
                locations.addAll(List.of(
                        "",
                        "C:\\Program Files (x86)\\Mozilla Firefox",
                        "C:\\Program Files (x86)\\Firefox Developer Edition",
                        "C:\\Program Files (x86)\\Mozilla Thunderbird",
                        "C:\\Program Files (x86)\\Nightly",
                        "C:\\Program Files (x86)\\SeaMonkey",
                        "C:\\Program Files (x86)\\Waterfox"
                ));
            }
            String userHome = System.getProperty("user.home");
            locations.addAll(List.of(
                    "",
                    userHome+ "\\AppData\\Local\\Mozilla Firefox",
                    userHome+ "\\AppData\\Local\\Firefox Developer Edition",
                    userHome+ "\\AppData\\Local\\Mozilla Thunderbird",
                    userHome+ "\\AppData\\Local\\Nightly",
                    userHome+ "\\AppData\\Local\\SeaMonkey",
                    userHome+ "\\AppData\\Local\\Waterfox",
                    "C:\\Program Files\\Mozilla Firefox",
                    "C:\\Program Files\\Firefox Developer Edition",
                    "C:\\Program Files\\Mozilla Thunderbird",
                    "C:\\Program Files\\Nightly",
                    "C:\\Program Files\\SeaMonkey",
                    "C:\\Program Files\\Waterfox"
            ));

        }
    }
}
