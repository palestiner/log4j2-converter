package com.inmar.api.json.masker;

import org.apache.logging.log4j.message.Message;

public interface Masker {

    Message handleMessage(Message message);

}
