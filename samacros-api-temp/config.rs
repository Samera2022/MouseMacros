//! Configuration API definitions.

use crate::ApiError;

/// Configuration interface.
pub trait IConfig {
    fn get(&self, key: &str) -> Option<ConfigValue>;
    fn set(&mut self, key: &str, value: ConfigValue) -> Result<(), ApiError>;
    fn save(&self) -> Result<(), ApiError>;
    fn reload(&mut self) -> Result<(), ApiError>;
}

/// Configuration value enum.
#[derive(Debug, Clone)]
pub enum ConfigValue {
    String(String),
    Int(i64),
    Float(f64),
    Bool(bool),
    List(Vec<ConfigValue>),
    Map(std::collections::HashMap<String, ConfigValue>),
}
