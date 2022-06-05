package com.example.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class dbHelper extends SQLiteOpenHelper {
    static final String DB_Name = "HL_DB.db";
    private static final String TABLE_NAME_userTable = "userTable";
    private static final String TABLE_NAME_repairManTable = "repairManTable";

    public dbHelper(Context context, int version){
        super(context, DB_Name, null, version);
    }

   @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    //증명 여부 가져오기
    public int getIsproof(String u_id){
        int result = 0;
        SQLiteDatabase db = getReadableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT isproof FROM repairManTable WHERE id = '" + u_id +"'");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        return result;
    }

    public int getNumber(String u_id){
        int result = 0;
        SQLiteDatabase db = getReadableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT number FROM repairRequestTable WHERE id = '" + u_id +"'");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        return result;
    }

    //모든 제목 가져오기
    public ArrayList<ListItem> getAllTitles(){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT number, title, state FROM repairRequestTable ORDER BY number DESC");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        while(cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            titles.add(item);
        }  
        return titles;
    }

    //카테고리로 제목들 가져오기
    public ArrayList<ListItem> getTitles_category(int position, int max_position, String u_id){
        StringBuffer sb = new StringBuffer();
        if(position != max_position){
            sb.append("SELECT number, title, state FROM repairRequestTable WHERE object = '" + position +"' ORDER BY number DESC");
        }
        else
            sb.append("SELECT number, title, state FROM repairRequestTable WHERE userId = '" + u_id +"' ORDER BY number DESC");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        while(cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            titles.add(item);
        }
        return titles;
    }

    //검색으로 제목 가져오기
    public ArrayList<ListItem> getTitles_search(int position, String search_text){
        StringBuffer sb = new StringBuffer();

        if(position!=0)
            sb.append("SELECT number, titl, state FROM repairRequestTable WHERE title LIKE '%" + search_text +"%' AND object = '" + position + "' ORDER BY number DESC");
        else
            sb.append("SELECT number, title, state FROM repairRequestTable WHERE title LIKE '%" + search_text +"%' ORDER BY number DESC");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        while(cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            titles.add(item);
        }
        return titles;
    }

    //수리 의뢰 내용 가져오기
    public String getRequest(int number){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT userId, title, symptom, symptom_contents, object, state FROM repairRequestTable WHERE number = '" + number + "'");

        SQLiteDatabase db = getReadableDatabase();

        String result = "";
        Cursor cursor = db.rawQuery(sb.toString(), null);
        while(cursor.moveToNext()){
            result += cursor.getString(0) + "##,#" + cursor.getString(1) + "##,#" + cursor.getInt(2) + "##,#" + cursor.getString(3)+ "##,#" + cursor.getInt(4)+ "##,#" + cursor.getInt(5);
        }
        return result;
    }

    //수리 의뢰 DB 저장
    public void insertRequest( String u_id,  String title, int symptom, String symptom_contents, int object, int tag){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO repairRequestTable(userID,title,object,symptom,symptom_contents,state) VALUES ('"+u_id+"',"+"'"+title+"','"+object+"','"+symptom+"','"+symptom_contents+"','"+tag+"');";
        db.execSQL(sql);
    }

    //수리 의뢰 수정(업데이트)
    public void updateRequest(SQLiteDatabase db, String u_id,  String title, int symptom, String symptom_contents, int object, int number){
        String sql = "UPDATE repairRequestTable SET (title,object,symptom,symptom_contents) = ('"+title+"'"+","+object+","+symptom+","+"'"+symptom_contents+"'"+") WHERE number = '" +number+"';";
        db.execSQL(sql);
    }

    //수리의뢰 삭제
    public void deleteRequest(int number){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM repairRequestTable WHERE number = '"+ number +"'";
        String sql2 = "UPDATE repairRequestTable SET number = number - 1 WHERE number > '" +number+"'";
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    public boolean isProofApprove(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isproof", 1);
        db.update(TABLE_NAME_repairManTable, contentValues, "id = ?", new String[] {id});
        db.close();
        return true;
    }

    public boolean isProofRefusal(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isproof", 0);
        db.update(TABLE_NAME_repairManTable, contentValues, "id = ?", new String[] {id});
        db.close();
        return true;
    }

    public boolean userPwUpdate(String id, String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pw", pw);
        db.update(TABLE_NAME_userTable, contentValues, "id = ?", new String[] {id});
        db.close();
        return true;
    }

    public boolean repairManPwUpdate(String id, String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pw", pw);
        db.update(TABLE_NAME_userTable, contentValues, "id = ?", new String[] {id});
        db.close();
        return true;
    }

    public long satisfiedUpdate(String id, int statePopup, int kindnessPopup, int termPopup, int pricePopup) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT satisfiedState, satisfiedKindness, satisfiedTerm, price, repairNumber FROM repairManTable WHERE id = 'KWH2'",null);

        int state = 0;
        int kindness = 0;
        int term = 0;
        int price = 0;
        int repairNumber = 0;

        while (cursor.moveToNext()) {
            state = cursor.getInt(0);
            kindness = cursor.getInt(1);
            term = cursor.getInt(2);
            price = cursor.getInt(3);
            repairNumber = cursor.getInt(4);
        }
        cursor.close();

        ContentValues contentValues = new ContentValues();
        contentValues.put("satisfiedState", (state + statePopup) / (repairNumber + 1));
        contentValues.put("satisfiedKindness", (kindness + kindnessPopup) / (repairNumber + 1));
        contentValues.put("satisfiedTerm", (term + termPopup) / (repairNumber + 1));
        contentValues.put("price", (price + pricePopup) / (repairNumber + 1));
        contentValues.put("repairNumber", repairNumber + 1);
        long result = db.update(TABLE_NAME_repairManTable, contentValues, "id = ?", new String[] {id});
        db.close();

        return result;
    }

    public void reportRequest(Integer number){
        SQLiteDatabase db = getWritableDatabase();
        String sql1 = "UPDATE repairRequestTable SET declar = declar + 1 WHERE number = " +number+"";
        db.execSQL(sql1);
        Cursor cursor = db.rawQuery("SELECT declar FROM repairRequestTable WHERE number = "+number+";", null);
        while(cursor.moveToNext()){
            if (cursor.getInt(0) >= 3) {
                String sql2 = "DELETE FROM repairRequestTable WHERE number = "+number+";";
                db.execSQL(sql2);
            }
        }
        db.close();
    }
    //민석 삽입
    //의뢰제목도 찾음
    public ArrayList<fragmentListItem> getRepairSuggestionTableData(String u_id){
        //db 다시 생각해야함 u_id에 해당하는 repairRequestTable 의뢰제목 내용 모두 가져와야함
        //동시에 repairSuggetionTable에서 그 의뢰에 해당하는 e_pay, r_details를 (p_num)으로 검색
        //
        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase db2 = getReadableDatabase();
        SQLiteDatabase db3 = getReadableDatabase();
        ArrayList<fragmentListItem> proposalList = new ArrayList<fragmentListItem>();
        /*
        ArrayList<requestListItem> getRequestList = getRequestName(u_id);
        Cursor.requestCursor = db.rawQuery
        */
        StringBuffer sb2 = new StringBuffer();//2,3,4가 state kindness termavg
        sb2.append("SELECT number, title, state from repairRequestTable Where userID="+"'"+u_id+"'and state!="+2+";"); //satis
        Cursor rR_cursor = db.rawQuery(sb2.toString(),null);

        while(rR_cursor.moveToNext()){

            StringBuffer sb = new StringBuffer();
            int p_num = rR_cursor.getInt(0);
            String r_name = rR_cursor.getString(1);
            sb.append("SELECT p_num, e_pay, r_details, r_id,u_check  from repairSuggestionTable Where p_num="+p_num+";");
            Cursor rS_cursor = db2.rawQuery(sb.toString(), null);
            while(rS_cursor.moveToNext()){//db에서 자료 가져와서 한 row객체<<당 4개의 자료(r_id, p_num, e_pay, r_details +추가

                StringBuffer sb3 = new StringBuffer();
                sb3.append("SELECT S_State, S_Kindness, S_Term FROM repairManTable Where id="+"'"+rS_cursor.getString(3)+"';");
                Cursor rM_cursor = db3.rawQuery(sb3.toString(),null);
                while(rM_cursor.moveToNext()){
                    fragmentListItem item = new fragmentListItem(rR_cursor.getString(1),rR_cursor.getInt(0),rS_cursor.getInt(1),
                            rS_cursor.getString(2), rS_cursor.getString(3),
                            rR_cursor.getInt(2),rM_cursor.getInt(0),rM_cursor.getInt(1),
                            rM_cursor.getInt(2),rS_cursor.getInt(4));
                    proposalList.add(item);
                }

            }
        }
        return proposalList;
    }
    public void updateStatesToZero(String r_id, int p_num){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase db2 = getReadableDatabase();
        String sql = "UPDATE repairSuggestionTable SET u_check=0 WHERE r_id="+"'"+r_id+"'and p_num="+p_num+";";
        db.execSQL(sql);
        String sql2 = "UPDATE repairRequestTable SET state=0 WHERE number="+p_num+";";
        db2.execSQL(sql2);
    }
    public void updateStatesToOne(String r_id, int p_num) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase db2 = getReadableDatabase();
        String sql = "UPDATE repairSuggestionTable SET u_check=1 WHERE r_id="+"'"+r_id+"'and p_num="+p_num+";";
        db.execSQL(sql);
        String sql2 = "UPDATE repairRequestTable SET state=1 WHERE number="+p_num+";";
        db2.execSQL(sql2);
    }

    public String getOpenLink(SQLiteDatabase db, String r_id) {//오픈링크 받아오는 메소드
        String sql= "SELECT openlink from repairManTable Where id="+"'"+r_id+"'"+";";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToNext();
        String openlink = cursor.getString(0);
        return openlink;
    }
 
    public void insertProposal(SQLiteDatabase db, String r_id, int p_num, int e_pay, String r_details){
        String sql = "INSERT INTO repairSuggestionTable VALUES"+"("+"'"+r_id+"'"+","+p_num+","+e_pay+","+"'"+r_details+"'"+");";
        db.execSQL(sql); 
    }
    //수리기사가 이미 신고한 글인지 확인
    public boolean checkReport(String id, Integer number){//수리기사 id, 게시글 번호
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM declarTable WHERE r_id = '"+id+"' AND p_num = '"+number+"';", null);
        while(cursor.moveToNext()){
            //신고이력이 존재하면 카우튼가 0보다 클것
            //true 리턴
            if (cursor.getCount() > 0) {
                db.close();
                return true;
            }
        }
        //존재하지 않으면 false를 리턴
        db.close();
        return false;
    }

    //기사가 게시글 신고
    public boolean reportRequest(String id, Integer number){//게시글 번호
        SQLiteDatabase db = getWritableDatabase();
        String sql1 = "UPDATE repairRequestTable SET declar = declar + 1 WHERE number = " +number+"";
        String sql2 = "INSERT INTO declarTable VALUES('"+id+"','"+number+"')";
        db.execSQL(sql1);
        db.execSQL(sql2);
        Cursor cursor = db.rawQuery("SELECT declar FROM repairRequestTable WHERE number = "+number+";", null);
        while(cursor.moveToNext()){
            //신고가 3번 누적되면 자동으로 게시글 삭제
            if (cursor.getInt(0) >= 3) {
                String sql3 = "DELETE FROM repairRequestTable WHERE number = "+number+";";
                String sql4 = "DELETE FROM declarTable WHERE p_num = "+number+";";
                db.execSQL(sql3);
                db.execSQL(sql4);
                db.close();
                return false;
            }
        }
        db.close();
        return true;
    }
}

