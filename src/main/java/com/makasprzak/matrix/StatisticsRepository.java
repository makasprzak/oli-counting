package com.makasprzak.matrix;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.FileUtils.readFileToString;

public class StatisticsRepository {
    private final Gson GSON = new Gson();

    public void save(List<Statistic> statistics) throws IOException {
        String serialized = GSON.toJson(statistics);
        FileUtils.writeStringToFile(new File(new Date().getTime() + ".json"), serialized);
    }


    public Optional<List<Statistic>> findLatest() {
        return listFiles(new File(System.getProperty("user.dir")), new String[]{"json"}, false).stream()
                .sorted((left, right) -> right.compareTo(left))
                .findFirst()
                .map(this::readStatistics);
    }

    private List<Statistic> readStatistics(File file) {
        try {
            return GSON.fromJson(readFileToString(file), new TypeToken<List<Statistic>>() {
            }.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
