package kr.co.killers.text;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
    private static Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * Vo Object 직렬화
     * 
     * @param obj
     * @return
     */
    public static String generateJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * requestBody를 객체로 변환한다.
     * 
     * @param body
     * @return
     */
    public static Object parseRequestJson(String body, Class<?> classzz) {
        return GSON.fromJson(body, classzz);
    }

    /**
     * requestBody를 List 객체로 변환한다
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> getList(String json,Class<T[]> clazz) {
        T[] jsonToObject = GSON.fromJson(json, clazz);
        return Arrays.asList(jsonToObject);
    }
}
