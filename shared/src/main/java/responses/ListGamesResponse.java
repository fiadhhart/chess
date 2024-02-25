package responses;

import java.util.List;

public class ListGamesResponse extends BaseResponse{
    private List<GameResponse> games;


    public ListGamesResponse(){}

    public ListGamesResponse(List<GameResponse> games) {
        this.games = games;
    }

    public ListGamesResponse(String message){
        super(message);
    }


    public List<GameResponse> getGames() {
        return games;
    }
}
