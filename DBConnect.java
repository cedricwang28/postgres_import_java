import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Date;  
import java.text.SimpleDateFormat;  
import java.util.Calendar;  




public class DBConnect{
    public static void main(String[] args) throws Exception{
        
       
        Timer time = new Timer(false);

        time.schedule(new TimerTask(){
                public void run(){
                    Date cur = new Date();
                    String getDay = cur.toString().substring(8,10);
                    String getHour = cur.toString().substring(11,13);


                    String url = "https://dd.weather.gc.ca/air_quality/aqhi/ont/observation/realtime/csv/202007"+getDay+getHour+"_AQHI_ON_SiteObs.csv";
                    System.out.println(url);


                    try {
                        downloadUsingStream(url, "./ontario.csv");
                        
                        importData();
                           

                    }
                    catch(IOException e) {
                        e.printStackTrace();
                        System.out.println("page not found");
                    }
                    
                }
        },new java.util.Date(),1000*60*60);

        
       
        importData();

 
    }


    public static void importData() throws IOException{
        Connection connection = null;
        PreparedStatement statement;
        PreparedStatement statement2;
        ResultSet rs;
        int batchSize = 20;

        String url = "jdbc:postgresql://localhost:5432/transnomis";
        String user = "postgres";
        String password = "651125";

        try {
 
            try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        connection = DriverManager.getConnection(url,user,password);

        connection.setAutoCommit(false);
        String sql = "INSERT INTO observation (date, hour, faffd,falif,falji) VALUES (?, ?, ?,?,?)";
        String ifExist = "select * from observation where date = ? and hour = ?";
        statement = connection.prepareStatement(sql);
        statement2 = connection.prepareStatement(ifExist);
        BufferedReader lineReader = new BufferedReader(new FileReader("./ontario.csv"));

        String lineText = null;
 
            int count = 0;
 
            lineReader.readLine(); 
 
            while ((lineText = lineReader.readLine()) != null) {
                count++;
                String[] data = lineText.split(",");
                String date = data[0];
                String hour = data[1];
                String faffd = data[2];
                String falif = data[3];
                String falji = data[4];

                statement2.setString(1, date);
                statement2.setString(2, hour);

                rs = statement2.executeQuery();
                if(rs.next()){
                    continue;
                }

                
                statement.setString(1, date);
                statement.setString(2, hour);
                statement.setString(3, faffd);
                statement.setString(4, falif);
                statement.setString(5, falji);

               
                statement.addBatch();
 
                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();
            statement.executeBatch();

            connection.commit();
            connection.close();
 
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
 
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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


    


}



