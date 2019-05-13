package network;

import java.io.Serializable;

public enum RequestType implements Serializable{
    OPERATION,
    PING,
    PONG,
    PEER
}
