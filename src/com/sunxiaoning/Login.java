package com.sunxiaoning;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    private JLabel jname = new JLabel("用户名:");
    private JLabel jpwd = new JLabel("密   码:");
    private JButton jdl = new JButton("登录");
    private JButton jzc = new JButton("注册");
    private JButton jxgma = new JButton("修改密码");
    private JButton jscyh = new JButton("删除用户");
    private JTextField jtname = new JTextField();
    private JTextField jtpwd = new JTextField();
    private JLabel ts = new JLabel();


    /**
     * 设置面板
     */
    public Login() {
        JFrame frame = new JFrame("个人通讯录登录");// 创建 JFrame 实例
        frame.setSize(400, 300);//设置页面大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//页面关闭
        //创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);//边设置布局为 null
        this.setResizable(true);
        // 添加面板
        frame.add(panel);
        //添加组件
        jname.setBounds(30,30,60,30);
        panel.add(jname);
        jpwd.setBounds(30,60,60,30);
        panel.add(jpwd);
        jdl.setBounds(30,100,130,30);
        panel.add(jdl);
        jdl.addActionListener(this);
        jzc.setBounds(200,100,130,30);
        panel.add(jzc);
        jzc.addActionListener(this);
        jxgma.setBounds(30,160,130,30);
        jxgma.addActionListener(this);
        panel.add(jxgma);
        jscyh.setBounds(200,160,130,30);
        jscyh.addActionListener(this);
        panel.add(jscyh);
        jtname.setBounds(100,30,230,30);
        panel.add(jtname);
        jtpwd.setBounds(100,60,230,30);
        panel.add(jtpwd);
        ts.setBounds(100,180,230,90);
        panel.add(ts);

        // 设置界面可见
        frame.setVisible(true);
    }


    /**
     * 点击事件的判断
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String user = jtname.getText().trim();
        String pwd = jtpwd.getText().trim();
        if("".equals(user)||"".equals(pwd)){
            ts.setText("用户名密码不能为空");
        }else {
            /**
             * 判断登录
             */
            if(e.getSource()==jdl){
                User u = findUser(user,pwd);
                if(u!=null){
                    ts.setText("");
                }else{
                    ts.setText("登陆失败,账号或密码错误");
                    System.out.print("登陆失败");
                }
            }else if(e.getSource()==jzc){
                if(findUser(user,pwd)!=null){
                    ts.setText("用户已经存在，请直接登录");
                }else{
                    int i = insertUser(user,pwd);
                    if(i>0){
                        ts.setText("注册成功，请直接登录");
                    }else{
                        ts.setText("服务器繁忙，请稍后再来");
                    }
                }
            }else if(e.getSource()==jxgma){
                if(findUser(user,pwd)!=null){
                    String newPassword = JOptionPane.showInputDialog(this,"修改密码","请输入新的密码",JOptionPane.PLAIN_MESSAGE);
                    int i = updatePaw(user,newPassword);{

                        if(i>0){
                            ts.setText("修改成功");
                        }else {
                            ts.setText("修改失败");

                        }
                    }
                    System.out.print(user);
                    System.out.print(newPassword);
                }else {
                    ts.setText("账号或密码错误");

                }
            }else if(e.getSource()==jscyh){
                if(findUser(user,pwd)!=null){
                    int i = userDelete(user);
                    if(i>0){
                        ts.setText("删除成功");
                    }else{
                        ts.setText("删除失败");
                    }
                }
            }
        }
    }

    /**
     * 查找用户
     * @param name
     * @param pwd
     * @return
     */
    public User findUser(String name,String pwd){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User u = null;
        try {
            conn = DButil.getConnection();//得到连接对象Connection
            String sql ="SELECT * FROM user WHERE USERNAME =? AND PASSWORD=?";
            stmt = conn.prepareStatement(sql);//得到执行sql语句的对象Statement
            //给？赋值
            stmt.setString(1, name);
            stmt.setString(2, pwd);

            rs = stmt.executeQuery();//执行sql语句
            if(rs.next()){
                u = new User();
                u.setId(rs.getInt(1));
                u.setName(rs.getString(2));
                u.setPassword(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DButil.closeAll(rs, stmt, conn);
        }

        return u;
    }

    /**
     * 插入数据
     */
    public int insertUser(String uesrname,String pwd){
        Connection conn = null;
        PreparedStatement stmt = null;
        int i = 0;
        try {
            conn = DButil.getConnection();
            stmt = conn.prepareStatement("INSERT INTO USER (username,PASSWORD) VALUES (?,?);");
            stmt.setString(1, uesrname);
            stmt.setString(2, pwd);
            i = stmt.executeUpdate();
            System.out.print(i);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DButil.closeAll(null, stmt, conn);
        }
        return i;
    }

    /**
     * 修改密码
     */
    public int updatePaw(String username,String password){
        Connection conn = null;
        PreparedStatement stmt = null;
        int i = 0;
        try {
            conn = DButil.getConnection();
            stmt = conn.prepareStatement("UPDATE USER SET PASSWORD=? WHERE username=?");
            stmt.setString(1,password );
            stmt.setString(2, username);

            i = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DButil.closeAll(null, stmt, conn);
        }
        return i;
    }

    /**
     * 删除用户
     * @param username
     */
    public int userDelete(String username){
        String sqldelString="delete from USER where username=?";
        Connection connection=null;
        PreparedStatement psmt=null;
        int i = 0;
        try {
            connection=DButil.getConnection();
            psmt=connection.prepareStatement(sqldelString);
            psmt.setString(1, username); //设置参数
            i =  psmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DButil.closeAll(null, psmt, connection);
        }
        return i;
    }
    public static void main(String[] argc){
       new Login();
    }
}
