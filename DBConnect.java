import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;



public class DBConnect{
    public static void main(String[] args) throws Exception{
        


        String url = "https://dd.weather.gc.ca/air_quality/aqhi/ont/observation/realtime/csv/2020070200_AQHI_ON_SiteObs.csv";

        

        Timer time = new Timer(false);

        time.schedule(new TimerTask(){
                public void run(){
                    // downloadUsingStream(url, "./ontario.csv");
                    System.out.println(new java.util.Date());
                }
        },new java.util.Date(),1000*60*60);


        // readCSV();

        PGConnection();



    }

    

    public static List<KGInfo> readCSV() {
        try {
            List<KGInfo> kgInfoList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("./ontario.csv"));
            reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                KGInfo kgInfo=new KGInfo();
                String item[] = line.split(",");
                kgInfo.setDate(item[0]);
                kgInfo.setHour(item[1]);
                kgInfo.setFaffd(item[2]);

                kgInfoList.add(kgInfo);
                
                // int value = Integer.parseInt(last);

                // list.add(last);
                // System.out.println(last);
            }
            System.out.println(list);
            
        } catch (Exception e) {
        }
        return kgInfoList;
        
    }

    public static void downloadUsingStream(String urlStr, String file) throws IOException{
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }


    public static void PGConnection()  throws Exception{
        Connection con;
        Statement st;
        ResultSet rs;

        String url = "jdbc:postgresql://localhost:5432/transnomis";
        String user = "postgres";
        String password = "651125";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List list = readCSV();

        con = DriverManager.getConnection(url,user,password);
        con.setAutoCommit(false);
        PreparedStatement prep = conn.prepareStatement("INSERT INTO observation (date,hour,faffd)  VALUES (?,?,?)");
        int num=0;
        for (KGInfo value : list) {
                num++;
                prep.setString(1, value.getDate());
                prep.setInteger(2,value.getHour());
                prep.setDouble(3,value.getFaffd());
                prep.addBatch();
                if(num>100){
                    System.out.println(prep);
                    prep.executeBatch();
                    conn.commit();
                    num=0;
                }
                System.out.println(prep);
                prep.executeBatch();
                conn.commit();
        }


        rs.close();
        st.close();
        con.close();
    }


}



class KGInfo{
    
}