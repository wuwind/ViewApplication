package com.wuwind.viewapplication;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.wuwind.corelibrary.db.DBHelper;
import com.wuwind.corelibrary.db.DBService;
import com.wuwind.corelibrary.utils.LogUtil;
import com.wuwind.viewapplication.module.db.entity.DaoMaster;
import com.wuwind.viewapplication.module.db.entity.Note;
import com.wuwind.viewapplication.module.db.entity.NoteDao;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.Property;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private DBService<Note> dbService;

    public ApplicationTest() {
        super(Application.class);


    }

    public void testAll() {
//        for (int i = 0; i < 20; i++) {
//        testAdd();
//
//        }
//        testDelete();
//        testUpdate();
//        testReadAll();
    }

    public void testFindBy() {

        DBHelper.getInstance().init(getContext(), "feng", DaoMaster.class.getName());
        DBHelper.getInstance().setDebugMode(true);
        dbService = DBService.getInstance(Note.class);

        Note note = dbService.findById(88);

//        for (Note note : list) {
        LogUtil.e(0, "note id+text:" + note.getId() + note.getText());
//        }
    }

    public void testUpdate() {

        DBHelper.getInstance().init(getContext(), "feng", DaoMaster.class.getName());
        DBHelper.getInstance().setDebugMode(true);
        dbService = DBService.getInstance(Note.class);

        List<Note> noteList = new ArrayList<>();
        Note note2 = new Note();
        Note note3 = new Note();
        Note note4 = new Note();
        note2.setId(80l);
        note2.setText("吧吧吧他他他他");
        note3.setId(84l);
        note3.setText("他他他他");
        note4.setId(88l);
        note4.setText("吃吃吃吃");

        noteList.add(note2);
        noteList.add(note3);
        noteList.add(note4);
        dbService.update(noteList);
    }

    public void testAdd() {
        LogUtil.e(0, "testAdd");

        Note note = new Note();
        note.setText("啊");
        dbService.insert(note);


        List<Note> list = new ArrayList<>();
        Note note2 = new Note();
        Note note3 = new Note();
        Note note4 = new Note();
        note2.setText("吧");
        note3.setText("他");
        note4.setText("吃");
//        note2.setId(6l);
//        note3.setId(7l);
//        note4.setId(8l);
        list.add(note2);
        list.add(note3);
        list.add(note4);
        dbService.insertList(list);
    }

    public void testDelete() {
        DBHelper.getInstance().init(getContext(), "feng", DaoMaster.class.getName());
        DBHelper.getInstance().setDebugMode(true);
        DBService dbService = DBService.getInstance(Note.class);

        dbService.delete(NoteDao.Properties.Text, "啊", "他", "吧");
    }

    public void testReadAll() {
        LogUtil.e(0, "readAll");

        DBHelper.getInstance().init(getContext(), "feng", DaoMaster.class.getName());
        DBService dbService = DBService.getInstance(Note.class);

        List<Note> list = dbService.findAll();

        for (Note note : list) {
            LogUtil.e(0, "note id+text:" + note.getId() + note.getText());
        }
    }

    public void testReadDescAll() {
        LogUtil.e(0, "testReadDescAll");

        DBHelper.getInstance().init(getContext(), "feng", DaoMaster.class.getName());
        DBHelper.getInstance().setDebugMode(true);
        DBService<Note> dbService = DBService.getInstance(Note.class);


//        WhereCondition condition = new WhereCondition.PropertyCondition().
//        new WhereCondition.PropertyCondition()
//        List<Note> list = dbService.findListDesc(NoteDao.Properties.Id);

        List<Property> properties = new ArrayList<>();
        properties.add(NoteDao.Properties.Id);
        properties.add(NoteDao.Properties.Text);
        List<Object> values = new ArrayList<>();
        values.add(84);
        values.add("吃");
        List<Note> list = dbService.findListOr(properties, values);
//        Note note = dbService.find(properties,  values);

        for (Note note : list) {
            LogUtil.e(0, "note id+text:" + note.getId() + note.getText());
        }
    }

    public void testReadId() {
        LogUtil.e(0, "testReadId");

        DBHelper.getInstance().init(getContext(), "feng", DaoMaster.class.getName());
        DBService<Note> dbService = DBService.getInstance(Note.class);

        List<Note> list = dbService.findBetween(NoteDao.Properties.Id, 80, 100);

        for (Note note : list) {
            LogUtil.e(0, "note id+text:" + note.getId() + note.getText());
        }
    }

    public void testLike() {
        LogUtil.e(0, "testReadId");

        DBHelper.getInstance().init(getContext(), "feng", DaoMaster.class.getName());
        DBHelper.getInstance().setDebugMode(true);
        DBService<Note> dbService = DBService.getInstance(Note.class);
        List<Note> list = dbService.findLikeList(NoteDao.Properties.Id, 8);
        for (Note note : list) {
            LogUtil.e(0, "note id+text:" + note.getId() + note.getText());
        }

    }

}