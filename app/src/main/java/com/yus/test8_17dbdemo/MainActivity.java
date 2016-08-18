package com.yus.test8_17dbdemo;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.greendao.query.Query;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 官方推荐将获取 DaoMaster 对象的方法放到 Application 层，这样将避免多次创建生成 Session 对象
        setupDatabase();
        // 插入操作，简单到只要你创建一个 Java 对象
        Note note = new Note(null, "text", "comment", new Date());
        getNoteDao().insert(note);
        //查询
        Query<Note> noteQuery = getNoteDao().queryBuilder()
                .where(NoteDao.Properties.Text.eq("text"))
                .orderAsc(NoteDao.Properties.Date)
                .build();
        List<Note> list = noteQuery.list();
    }

    private void setupDatabase() {
// 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        //建立数据库,指定名字
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "people-db", null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
    private NoteDao getNoteDao() {
        return daoSession.getNoteDao();
    }
}
