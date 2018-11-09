package hi.im.jenga.member.controller;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;

class MongoTest {

    private MongoTemplate mongoTemplate;

    public MongoTest() {

        String mongoContextPath = "/mongoConfig.xml";

        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(mongoContextPath);

        mongoTemplate = (MongoTemplate) ctx.getBean("mongoTemplate");
    }

    public static void main(String[] args) {
        MongoTest mongoTest = new MongoTest();

        System.out.println(mongoTest.mongoTemplate);

        mongoTest.insertTestData();
    }

    private void insertTestData() {
        MongoTestVO testVO = new MongoTestVO();
        testVO.setName("둘리");
        testVO.setAddress("고길동 집");

        mongoTemplate.insert(testVO, "person");
    }

    private class MongoTestVO {

        @Id
        private String id;

        private String name;
        private String address;

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
    }

}
