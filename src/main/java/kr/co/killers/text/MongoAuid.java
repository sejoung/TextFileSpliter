package kr.co.killers.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoAuid {
    private static final Logger logger = LoggerFactory.getLogger(MongoAuid.class);

    private static final int COLLECTION_COUNT = 20;
    private static final String COLLECTION_PREFIX = "CIData_";

    private static final String CATE = "cate";
    private static final String GEN = "gender";
    private static final String AGE = "age";

    private static Map<String, MongoCollection<Document>> collectionMap = new HashMap<>();

    public int getPartition(String key) {
        return Math.abs(key.hashCode() % COLLECTION_COUNT);
    }

    public String getName(String key) {

        if ("368".equals(key)) {
            return "e스포츠/게임";
        } else if ("369".equals(key)) {
            return "IT/인터넷/통신";
        } else if ("370".equals(key)) {
            return "건강";
        } else if ("371".equals(key)) {
            return "경제";
        } else if ("372".equals(key)) {
            return "교육";
        } else if ("373".equals(key)) {
            return "금융";
        } else if ("374".equals(key)) {
            return "모바일";
        } else if ("375".equals(key)) {
            return "문화";
        } else if ("376".equals(key)) {
            return "부동산";
        } else if ("377".equals(key)) {
            return "사회";
        } else if ("378".equals(key)) {
            return "생활";
        } else if ("379".equals(key)) {
            return "세계";
        } else if ("380".equals(key)) {
            return "스포츠";
        } else if ("381".equals(key)) {
            return "엔터테인먼트";
        } else if ("382".equals(key)) {
            return "여행";
        } else if ("383".equals(key)) {
            return "영화/뮤직";
        } else if ("384".equals(key)) {
            return "음식";
        } else if ("385".equals(key)) {
            return "자동차";
        } else if ("386".equals(key)) {
            return "정치";
        } else if ("387".equals(key)) {
            return "종교";
        } else if ("388".equals(key)) {
            return "취업";
        } else if ("389".equals(key)) {
            return "컴퓨터";
        } else if ("390".equals(key)) {
            return "패션/뷰티";
        }
        return "";

    }

    private String getcollectionName(String auid) {
        int idx = this.getPartition(auid);
        String collectionName = COLLECTION_PREFIX + String.format("%02d", idx);
        return collectionName;
    }

    private Map<String, String> getData(String auid) {
        Map<String, String> result = new HashMap<>();
        MongoClientURI connectionString = new MongoClientURI("mongodb://mongo:10001");

        try (MongoClient mongoClient = new MongoClient(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("userlog");

            for (int i = 0; i < 20; i++) {
                String tempCollectionName = COLLECTION_PREFIX + String.format("%02d", i);
                collectionMap.put(tempCollectionName, database.getCollection(tempCollectionName));
            }

            String collectionName = this.getcollectionName(auid);

            MongoCollection<Document> collection = collectionMap.get(collectionName);
            Document query = new Document().append("_id", auid);

            Document document = collection.find(query).first();
            if (document != null) {
                Object ikc = document.get("iKc");
                if (ikc != null) {
                    List<Document> datas = (List) ((Document) ikc).get("data");

                    StringBuffer sb = new StringBuffer();
                    datas.forEach((data) -> {

                        sb.append((this.getName(data.getString("cate")) + " cnt = " + data.get("cnt") + " "));

                    });
                    result.put(CATE, sb.toString());
                } else {
                    result.put(CATE, "카테고리 없음");

                }

                Object iGen = document.get("iGender2");
                if (iGen != null) {
                    String gender = ((Document) iGen).get("data").toString();
                    logger.debug(gender);
                    result.put(GEN, gender);

                } else {
                    result.put(GEN, "성별 없음");

                }

                Object iAge = document.get("iAge2");

                if (iAge != null) {
                    String age = ((Document) iAge).get("data").toString();
                    logger.debug(age);
                    result.put(AGE, age);

                } else {
                    result.put(AGE, "나이 없음");

                }
            } else {
                result.put(CATE, "카테고리 없음");
                result.put(GEN, "성별 없음");
                result.put(AGE, "나이 없음");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    private void run() {

        try (OPCPackage pkg = OPCPackage.open(new File("D://test.xlsx"))) {
            Workbook workbook = new XSSFWorkbook(pkg);
   
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {


                Cell auidcell = row.getCell(3);
                Cell catecell = row.getCell(6);
                Cell gencell = row.getCell(7);
                Cell agecell = row.getCell(8);

                String auid = auidcell.getStringCellValue();

                Map<String, String> map = getData(auid);
                
                catecell.setCellType(CellType.STRING);
                catecell.setCellValue(map.get(CATE));
                
                gencell.setCellType(CellType.STRING);
                gencell.setCellValue(map.get(GEN));
                
                agecell.setCellType(CellType.STRING);
                agecell.setCellValue(map.get(AGE));

            }
            
            
            try (OutputStream fileOut = new FileOutputStream("D://workbook.xls")) {
                workbook.write(fileOut);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MongoAuid ma = new MongoAuid();
        ma.run();
    }
}
