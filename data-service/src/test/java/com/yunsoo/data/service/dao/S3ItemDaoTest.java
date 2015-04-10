package com.yunsoo.data.service.dao;

/**
 * Created by:   Lijian
 * Created on:   2015/2/13
 * Descriptions:
 */
public class S3ItemDaoTest {

//    private S3ItemDao s3ItemDao;
//
//    @Autowired
//    private AmazonSetting amazonSetting;
//
//    @Before
//    public void setUp() throws Exception {
//        ApplicationContext applicationContext = SpringAppContextUtil.getApplicationContext();
//        s3ItemDao = (S3ItemDao) applicationContext.getBean("s3ItemDao");
//    }
//
//    @Test
//    public void test_listBucket() {
//        List<Bucket> buckets = s3ItemDao.getBuckets();
//        if (buckets != null) {
//            buckets.forEach(item -> {
//                System.out.println(item.getName());
//            });
//        }
//    }
//
//
//    @Test
//    public void test_putItem() throws Exception {
//        String bucketName = amazonSetting.getS3_basebucket();
//        String key = "test_path/test_key1";
//        String item = "test";
//
//        s3ItemDao.putItem(bucketName, key, item);
//
//        String result = s3ItemDao.getItem(bucketName, key, String.class);
//        System.out.println(result);
//    }
}
