package kr.co.killers.text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoAuid {

    private static final int COLLECTION_COUNT = 20;
    private static final String COLLECTION_PREFIX = "CIData_";

    private static Map<String, MongoCollection<Document>> collectionMap = new HashMap<>();

    public int getPartition(String key) {
        return Math.abs(key.hashCode() % COLLECTION_COUNT);
    }

    public String getName(String key) {

        switch (key) {
        case "368":
            return "e스포츠/게임";
        case "369":
            return "IT/인터넷/통신";
        case "370":
            return "건강";
        case "371":
            return "경제";
        case "372":
            return "교육";
        case "373":
            return "금융";
        case "374":
            return "모바일";
        case "375":
            return "문화";
        case "376":
            return "부동산";
        case "377":
            return "사회";
        case "378":
            return "생활";
        case "379":
            return "세계";
        case "380":
            return "스포츠";
        case "381":
            return "엔터테인먼트";
        case "382":
            return "여행";
        case "383":
            return "영화/뮤직";
        case "384":
            return "음식";
        case "385":
            return "자동차";
        case "386":
            return "정치";
        case "387":
            return "종교";
        case "388":
            return "취업";
        case "389":
            return "컴퓨터";
        case "390":
            return "패션/뷰티";
        default:
            return "";
        }

    }

    private String getcollectionName(String auid) {
        int idx = this.getPartition(auid);
        String collectionName = COLLECTION_PREFIX + String.format("%02d", idx);
        return collectionName;
    }

    private void run() {

        try {

            MongoClientURI connectionString = new MongoClientURI("mongodb://mongo:10001");
            MongoClient mongoClient = new MongoClient(connectionString);

            MongoDatabase database = mongoClient.getDatabase("userlog");

            for (int i = 0; i < 20; i++) {
                String tempCollectionName = COLLECTION_PREFIX + String.format("%02d", i);
                collectionMap.put(tempCollectionName, database.getCollection(tempCollectionName));
            }

            String auid = "f850868914fd52e66567176516825d30ee0-2288";

            String collectionName = this.getcollectionName(auid);

            MongoCollection<Document> collection = collectionMap.get(collectionName);

            Document query = new Document().append("_id", auid);

            Document document = collection.find(query).first();
            if (document != null) {
                if (document.get("iKc") != null) {
                    List<Document> datas = (List) ((Document) document.get("iKc")).get("data");

                    StringBuffer sb = new StringBuffer();
                    datas.forEach((data) -> {

                        sb.append((this.getName(data.getString("cate")) + " cnt = " + data.get("cnt") + " "));

                    });
                    System.out.println(sb.toString());
                } else {
                    System.out.println("카테고리 없음");
                }

                if (document.get("iGender2") != null) {
                    System.out.println(((Document) document.get("iGender2")).get("data"));

                } else {
                    System.out.println("성별 없음");
                }

                if (document.get("iAge2") != null) {
                    System.out.println(((Document) document.get("iAge2")).get("data"));

                } else {
                    System.out.println("나이 없음");

                }
            } else {
                System.out.println("널러러러러러러");
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MongoAuid ma = new MongoAuid();
        ma.run();
    }
}
