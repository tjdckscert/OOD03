/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import deu.cse.spring_webmail.control.CommandType;
import jakarta.mail.Message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sooye
 */
public class ImportantMessageAgent extends MessageAgent {

    private static ImportantMessageAgent uniqueInstance = new ImportantMessageAgent();
    private static String userid = null;
    private boolean needInitialize = true;
    private static final Logger logger = Logger.getLogger(ImportantMessageAgent.class.getName());

    public String getUserid() {
        return userid;
    }

    private ImportantMessageAgent() {
    }

    public static ImportantMessageAgent getInstance(String userid) {
        ImportantMessageAgent.userid = userid;
        return uniqueInstance;
    }

    public static ImportantMessageAgent getInstance() {
        return uniqueInstance;
    }

    protected boolean getMsgIdListFromDB() {

        boolean status = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        if (this.isUserIdNull()) {
            return status;
        }

        try {
            super.setNeedUpdate(false);
            super.resetMsgIdList();

            Class.forName(CommandType.JDBCDRIVER);
            conn = DriverManager.getConnection(CommandType.JDBCURL, CommandType.JDBCUSER, CommandType.JDBCPASSWORD);

            String sql = "select msgid from Important_list where email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userid);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int buf_msgid = rs.getInt("msgid");
                super.addMsgId(buf_msgid);
            }

            rs.close();

            status = true;
            return status;

        } catch (Exception ex) {
            logger.log(Level.SEVERE,"ImportantMessageAgent.setMsgIdList Error : " + ex);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportantMessageAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportantMessageAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }

    public ArrayList<Message> getMessageList(Message[] messages) {
        ArrayList<Message> ImportantMessage = null;

        try {
            //처음실행이면 MsgIdList초기화.
            if (needInitialize) {
                getMsgIdListFromDB();
                needInitialize = false;
            }

            if (super.isNeedUpdate()) {
                if (getMsgIdListFromDB()) {
                    ImportantMessage = filter(messages, super.getMsgIdList());
                    return ImportantMessage;
                }
            } else {
                ImportantMessage = filter(messages, super.getMsgIdList());
                return ImportantMessage;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE,"ImportantMessageAgent.getMessageList Error" + ex);
        }
        return ImportantMessage;
    }

    protected ArrayList<Message> filter(Message[] messages, ArrayList<Integer> msgIdList) {
        ArrayList<Message> ImportantMessage = new ArrayList<Message>();
        for (int i = 0; i < msgIdList.size(); i++) {
            ImportantMessage.add(messages[msgIdList.get(i) - 1]);
        }
        logger.log(Level.INFO,"end filterling");

        return ImportantMessage;
    }

    protected boolean insertMsgId(int msgid) {
        boolean status = false;

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {

            if (isUserIdNull()) {
                return status;
            }
            Class.forName(CommandType.JDBCDRIVER);
            conn = DriverManager.getConnection(CommandType.JDBCURL, CommandType.JDBCUSER, CommandType.JDBCPASSWORD);

            String sql = "INSERT INTO Important_list VALUES (?,?)";
            pstmt = conn.prepareStatement(sql);
            if (userid != null && !(userid.equals(""))) { //email 값이 null이 아니면.
                pstmt.setString(1, userid);
                pstmt.setInt(2, msgid);
            }
            pstmt.executeUpdate();

            status = true;
            return status;
        } catch (Exception ex) {
            logger.log(Level.SEVERE,"ImportantMessageAgent.insertMsgId error : " + ex);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportantMessageAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportantMessageAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }

    protected boolean deleteMsgId(int msgid) {
        boolean status = false;

        if (isUserIdNull()) {
            return status;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommandType.JDBCDRIVER);
            conn = DriverManager.getConnection(CommandType.JDBCURL, CommandType.JDBCUSER, CommandType.JDBCPASSWORD);

            String sql = "DELETE FROM Important_list WHERE ('email' = ?) and ('msgid' = ?)";
            pstmt = conn.prepareStatement(sql);
            if (userid != null && !(userid.equals(""))) { //email 값이 null이 아니면.
                pstmt.setString(1, userid);
                pstmt.setInt(2, msgid);
            }
            pstmt.executeUpdate();

            status = true;
            return status;

        } catch (Exception ex) {
            logger.log(Level.SEVERE,"ImportantMessageAgent.deleteMsgId error : " + ex);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportantMessageAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportantMessageAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }

    public boolean addMessage(int msgid) {
        boolean status = false;

        try {
            if (!super.getMsgIdList().contains(Integer.valueOf(msgid))) {        //msgIdList에 추가된 적 없는 no면
                super.addMsgId(msgid);
                status = insertMsgId(msgid);
                super.setNeedUpdate(true);
                return status;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE,"ImportantMessageAgent.addMessage error : " + ex);
        }
        return status;

    }

    public boolean removeMessage(int msgid) {
        boolean status = false;
        try {
            if (super.getMsgIdList().contains(Integer.valueOf(msgid))) {           //msgIdList에 존재하는 메시지번호이면
                super.removeMsgId(msgid);
                status = deleteMsgId(msgid);
                super.setNeedUpdate(true);
                status = true;
                return status;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE,"ImportantMessageAgent.removeMessage error : " + ex);
        }
        return status;

    }

    private boolean isUserIdNull() {
        if (userid == null || userid.equals("")) {
            return true;
        }
        return false;

    }

    public void updateMsgId(int deletedMsgid) {
        for (int i = 0; i > this.getMsgIdSize();) {
            if (getMsgIdValue(i) > deletedMsgid) {
                this.updateMsgId(i, getMsgIdValue(i) - 1);
            }
            i++;
        }

        updateImportantListDB(deletedMsgid);
    }

    private boolean updateImportantListDB(int deletedMsgId) {
        boolean status = false;
        if (isUserIdNull()) {
            return status;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName(CommandType.JDBCDRIVER);
            conn = DriverManager.getConnection(CommandType.JDBCURL, CommandType.JDBCUSER, CommandType.JDBCPASSWORD);

            String sql = "update Important_list set msgid=msgid-1 where email=? and msgid>?;";
            pstmt = conn.prepareStatement(sql);
            if (userid != null && !(userid.equals(""))) { //email 값이 null이 아니면.
                pstmt.setString(1, userid);
                pstmt.setInt(2, deletedMsgId);
            }
            pstmt.executeUpdate();

            status = true;
            return status;

        } catch (Exception ex) {
            logger.log(Level.SEVERE,"ImportantMessageAgent.updateImportantListDB error : " + ex);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportantMessageAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportantMessageAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }

}//end ImportantMessageAgent
