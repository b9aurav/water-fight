package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {

  static class Self {
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate {
    public Links _links;
    public Arena arena;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!";
  }

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
    System.out.println(arenaUpdate);
    String command = "";
    int width = arenaUpdate.arena.dims.get(0);
    int height = arenaUpdate.arena.dims.get(1);
    int currentPosX = 0;
    int currentPosY = 0;
    String selfDirection = "";

    for(Map.Entry m:arenaUpdate.arena.state.entrySet()){  
       if(m.getKey().toString().equals(arenaUpdate._links.self.href)) {
           PlayerState selfState = (PlayerState)m.getValue();
            currentPosX = selfState.x;
            currentPosY = selfState.y;
            selfDirection = selfState.direction;

            if (selfState.wasHit) {
                if (currentPosX <= 1 || currentPosY <= 1) {
                    return "L";
                } else if (currentPosX >= width - 1 || currentPosY <= height - 1) {
                    return "R";
                }
                return "F";
            }
       }
    }  

    for(Map.Entry m:arenaUpdate.arena.state.entrySet()){  
        int oppPosX, oppPosY;
        String oppDirection;
        if(!m.getKey().toString().equals(arenaUpdate._links.self.href)) {
            PlayerState opponentState = (PlayerState)m.getValue();
            oppPosX = opponentState.x;
            oppPosY = opponentState.y;
            oppDirection = opponentState.direction;

            if (oppPosX >= currentPosX - 3 && oppPosX <= currentPosX - 1 &&  oppPosY == currentPosY) {
                if (selfDirection.equals("W")) {
                    return "T";
                } else if (selfDirection.equals("N")) {
                    return "L";
                } else if (selfDirection.equals("S")) {
                    return "R";
                }
            }
            if (oppPosX <= currentPosX + 3 && oppPosX >= currentPosX + 1 && oppPosY == currentPosY) {
                if (selfDirection.equals("E")) {
                    return "T";
                } else if (selfDirection.equals("N")) {
                    return "R";
                } else if (selfDirection.equals("S")) {
                    return "L";
                }
            }
            if (oppPosY >= currentPosY - 3 && oppPosY <= currentPosY - 1 && oppPosX == currentPosX) {
                if (selfDirection.equals("N")) {
                    return "T";
                } else if (selfDirection.equals("W")) {
                    return "R";
                } else if (selfDirection.equals("E")) {
                    return "L";
                }
            }
            if (oppPosY <= currentPosY + 3 && oppPosY >= currentPosY + 1 && oppPosX == currentPosX) {
                if (selfDirection.equals("S")) {
                    return "T";
                } else if (selfDirection.equals("W")) {
                    return "L";
                } else if (selfDirection.equals("E")) {
                    return "R";
                }
            }

            if (currentPosX > 2 && selfDirection == "N") {
                return "F";
            } else {
                if (currentPosX < 2) {
                    return "R";
                } else if (currentPosX > width - 2) {
                    return "L";
                }  else {
                    command = "R";
                }
            }

            if (currentPosX > 2 && selfDirection == "W") {
                return "F";
            } else {
                if (currentPosX < 2) {
                    return "R";
                } else if (currentPosX > height - 2) {
                    return "L";
                } else {
                    command = "L";
                }
            }

            if (currentPosY > 2 && selfDirection == "E") {
                return "F";
            } else {
                if (currentPosY < 2) {
                    return "R";
                } else if (currentPosY > width - 2) {
                    return "L";
                } else {
                    command = "R";
                }
            }

            if (currentPosY > 2 && selfDirection == "E") {
                return "F";
            } else {
                if (currentPosY < 2) {
                    return "R";
                } else if (currentPosY > height - 2) {
                    return "L";
                } else {
                    return "L";
                }
            }
        }
    }
    return command;
  }
}

