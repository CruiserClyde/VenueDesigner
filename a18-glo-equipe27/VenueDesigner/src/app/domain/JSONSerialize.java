package app.domain;

import java.io.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

final class JSONSerialize {
    private final ObjectMapper mapper = new ObjectMapper();

    void serializeToJson(Room room, String path) {
        try {
            mapper.writeValue(new File(path), room);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Room deserializeFromJson(String path) {
        try {
            return mapper.readValue(new File(path), Room.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String toJson(Room room) {
        try {
            return mapper.writeValueAsString(room);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    Room fromJson(String json) {
        try {
            return mapper.readValue(json, Room.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

