use irohcompose::{Core, EventHandler, Model};
use std::sync::Arc;

struct LogEventHandler;

impl EventHandler for LogEventHandler {
    fn on_update(&self, model: Model) {
        println!("on_update: {model:?}");
    }
}

#[tokio::main]
async fn main() {
    let event_handler = Arc::new(LogEventHandler);
    let core = Core::new(event_handler).unwrap();

    println!("ctrl+c to exit");

    tokio::signal::ctrl_c()
        .await
        .expect("failed to listen for event");
}
