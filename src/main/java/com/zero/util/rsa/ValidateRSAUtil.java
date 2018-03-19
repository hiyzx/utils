package com.zero.util.rsa;

public class ValidateRSAUtil {

    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoPooz8fZWT0FJYlu7SmSOvDvE2jDHVs2FKQbS8QAXZ04wlhJMGFMXY4WhaquJt/g8rdjcL6rQzIVH/G4gyv733N3yC4B4GWfwvbgM+qQoEXOXm+w7v07lLJk5PBAUri/Q4D6wiOFf1iWUNxSxTF+ip9Ex1ncXOiKCsEMdsOHEGwIDAQAB";
    public static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKg+ijPx9lZPQUliW7tKZI68O8TaMMdWzYUpBtLxABdnTjCWEkwYUxdjhaFqq4m3+Dyt2NwvqtDMhUf8biDK/vfc3fILgHgZZ/C9uAz6pCgRc5eb7Du/TuUsmTk8EBSuL9DgPrCI4V/WJZQ3FLFMX6Kn0THWdxc6IoKwQx2w4cQbAgMBAAECgYBScHql420yc5lpDQIn7tcBPekHl2aJdtYz5puKZz8FOMCOfCqxEMY+UqzbIHq3e+buTL1TfyPzNhQk5uNLvExcenW4LU6ambgR3TIMwwWf0qKHDykjh5l5bv0BXei+Iqsq0q025jBgf0HL9N/N7+ZdQKSD8hxkRfVm4W7EcPk8yQJBANQCNKQMkmCTsb6CQaCHrwwXxm+nxCeoN0mkgm1VcnOU4+7+8pbYUwq+fUnNvgNae4wvqMq2d4VvZsQQjmC08g0CQQDLJ5hLgVKbXiV+K6WCUXJpAw+aW2urKH4Zfu1cDZis5SnZOsFGQ4KS1QUIXg3jwFaq/Zj3+sR4H8hY20sBJQzHAkEA0fohQv2EmIyPdZ0y/JiGIdcGXZ3upExx/ckmuLzxb1Je8l2lzQsSLEkAc/GHKXEbfnvRlHxmIk10nem76kIsnQJAPz1vIin6vhS7lSAfIIccF2Z1h/ZneNtdDbC+UMaGvWOE+HNa0PWZAv4+19D3f+tSz12sLZ2pZT93jXmOSqfu5wJALWuntBN3HuJh0sOF4wARbpJG3Ppb/vfOIP0Q7tfthAAdaofZl73UMVMdCfWHXLEjyecBInGyrYD94RfOFZ87Ag==";

    /**
     * 解密参数并鉴权
     */
    public static String decByPrivateKey(String jsonData) throws Exception {
        String outputStr = null;
        // 在字符串为非空的情况下才进去解密
        if (jsonData != null && !"".equals(jsonData)) {
            byte[] jsonArray = Base64.decode(jsonData);
            // 参数字符串转成byte数组并进行解密
            byte[] decodedData = RSACoder.decryptByPrivateKey(jsonArray, privateKey);
            outputStr = new String(decodedData);
            // 根据指定私钥对数据进行签名
            String sign = RSACoder.sign(jsonArray, privateKey);
            // 验证签名
            boolean status = RSACoder.verify(jsonArray, publicKey, sign);
            if (!status) {
                outputStr = null;
            }
        } else {
            outputStr = null;
        }
        return outputStr;
    }
}
