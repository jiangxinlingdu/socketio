package com.corundumstudio.socketio.demo;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class JenkinsConnect {
    private JenkinsConnect() {
    }

    // 连接 Jenkins 需要设置的信息
    /**
     * 参考地址 ，非常不错http://www.mydlq.club/article/23/
     */
    static final String JENKINS_URL = "http://192.168.2.11:8080/jenkins/";
    static final String JENKINS_USERNAME = "admin";
    static final String JENKINS_PASSWORD = "123456";

    /**
     * Http 客户端工具
     * <p>
     * 如果有些 API 该Jar工具包未提供，可以用此Http客户端操作远程接口，执行命令
     *
     * @return
     */
    public static JenkinsHttpClient getClient() {
        JenkinsHttpClient jenkinsHttpClient = null;
        try {
            jenkinsHttpClient = new JenkinsHttpClient(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_PASSWORD);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jenkinsHttpClient;
    }

    /**
     * 连接 Jenkins
     */
    public static JenkinsServer connection() {
        JenkinsServer jenkinsServer = null;
        try {
            jenkinsServer = new JenkinsServer(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_PASSWORD);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jenkinsServer;
    }

    public static void main(String[] args) {
        JenkinsServer jenkinsServer = JenkinsConnect.connection();
        System.out.println(jenkinsServer);


        try {
            // 获取 Job 列表
//                Map<String, Job> jobs = jenkinsServer.getJobs();
//                for (Job job:jobs.values()){
//                    System.out.println(job.getName());
//                }

            JobWithDetails job = jenkinsServer.getJob("dev-rainbow-bootstrap");
            List<Build> builds = job.getBuilds();
            for (Build build : builds) {
                // job 的控制台日志信息
                System.out.println(build.details().getConsoleOutputHtml());
                System.out.println(build.details().getConsoleOutputText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
