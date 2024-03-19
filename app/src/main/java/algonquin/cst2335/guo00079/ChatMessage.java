package algonquin.cst2335.guo00079;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @ColumnInfo(name="message")
    protected String message;
    @ColumnInfo(name="TimeSent")
    protected String timeSent;
    @ColumnInfo(name="SendOrReceive")
    protected int sendOrReceive;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    public int getSendOrReceive() {
        return sendOrReceive;
    }

    public void setSendOrReceive(int sendOrReceive) {
        this.sendOrReceive = sendOrReceive;
    }

    public ChatMessage(String message, String timeSent, int sendOrReceive) {
        this.message = message;
        this.timeSent = timeSent;
        this.sendOrReceive = sendOrReceive;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public void setId(int id) {
        this.id = id;
    }
}


