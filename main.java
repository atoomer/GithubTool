import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
import github.tools.client.RequestParams;
import github.tools.responseObjects.*;


import java.awt.*;  
import java.awt.event.*;
import java.io.*;
import java.util.Scanner; 



public class Main {
  public static void main(String[] args) {
               
        Scanner scan = new Scanner(System.in);

        //create repo from existing project
        System.out.print("Choose a project that you would like to turn into a GitHub repo.\nEnter file path: ");
        String filePath = scan.next();
        File projectFile = new File(filePath);
        boolean windows = true;
        if (!projectFile.exists()){
            System.out.println("Something went wrong");
        }
        else{
            System.out.println("Repo will link to link to the folder at "+ filePath);
        }
        String projName="";
        for (int i = filePath.length(); i >=0; i--){
            if (filePath.charAt(i-1)=='\\'){
                windows = true;
                break;
            }
            else if(filePath.charAt(i-1)=='/'){
                windows = false;
                break;
            }
            else
                projName = (filePath.charAt(i-1)+projName);
        }

        //create subprocess client
        GitSubprocessClient gitSubprocessClient = new GitSubprocessClient(filePath);
    
        //git init at filePath
        String gitInit  = gitSubprocessClient.gitInit();
    
        //add .gitignore
        String gitIgnoreFilePath = "";

        if (windows == true) {
            gitIgnoreFilePath = (filePath+"\\.gitignore");
        }
        else if (windows == false){
            gitIgnoreFilePath = (filePath+"/.gitignore");
        }
        try {
            FileWriter gitignoreWriter = new FileWriter(gitIgnoreFilePath);
            gitignoreWriter.write("*.class\n*.log\n*.ctxt\n.mtj.tmp/\n*.jar\n*.war\n*.nar\n*.ear\n*.zip\n*.tar.gz\n*.rar\n*.vscode\nhs_err_pid*\nreplay_pid*");
            gitignoreWriter.close();
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    
        //add README.md
        String readmeFilePath = "";
        if (windows == true) {
            readmeFilePath = (filePath+"\\README.md");
        }
        else if (windows == false){
            readmeFilePath = (filePath+"/README.md");
        }
        try {
            FileWriter readmeWriter = new FileWriter(readmeFilePath);
            readmeWriter.write("# "+projName);
            readmeWriter.close();
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
        //git add and initial git commit
        String gitAddAll = gitSubprocessClient.gitAddAll();
        String commit = gitSubprocessClient.gitCommit("Initial Commit");

        //enter github username and api key
        System.out.print("Enter GitHub username and API token.\nusername: ");
        String username = scan.next();
        System.out.print("token: ");
        String apiToken = scan.next();


        //gitHubApiClient object
        GitHubApiClient gitHubApiClient = new GitHubApiClient(username, apiToken);

        //promt for new repo name in GitHub
        System.out.println("Enter the name for the GitHub repo: ");
        String repoName = scan.next();

        //prompt for private or public
        System.out.print("Will this be private or public? type 1 for private, 2 for public: ");
        int privacyChoice = scan.nextInt();
        boolean privacy = true;
        switch(privacyChoice){
            case 1:
                privacy = true;
                break;
            case 2:
                privacy = false;
                break;
            default:
                System.out.println("Something went wrong");
                System.exit(0);
        }

        //prompt for description
        System.out.print("Enter a discription for repo: ");
        String description = scan.next();
        //-----------------------------------------------------
        
        //creates repo with the entered parameters
        RequestParams requestParams = new RequestParams();
        requestParams.addParam("name", repoName);
        requestParams.addParam("description", description);
        requestParams.addParam("private", privacy);   
        CreateRepoResponse createRepoResponse = gitHubApiClient.createRepo(requestParams);

        //github url
        String gitHubUrl = ("https://github.com/"+username+"/"+repoName+".git");

        //git remote add origin <url>
        String gitRemoteAdd = gitSubprocessClient.gitRemoteAdd("origin", gitHubUrl);

        //push initial commit
        String push = gitSubprocessClient.gitPush("master");

        //print GitHub repo url
        System.out.println(gitHubUrl);
    }  
}
