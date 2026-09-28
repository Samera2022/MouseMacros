//! saMacros API definitions (mirror of Java API).
//!
//! This crate provides type definitions and traits that correspond to the
//! Java API in `samacros-api`. This ensures compatibility between Rust and
//! Java implementations.

pub mod script;
pub mod event;
pub mod config;
pub mod action;

/// Re-export commonly used types.
pub use script::{IScriptAPI, IScriptContext, ISystemInfo, IScreenInfo, IMouseInfo, IMacroInfo};
pub use event::{Event, EventHandler, EventPriority, Cancellable, Listener};
pub use config::IConfig;
pub use action::IMouseAction;

/// API error type.
#[derive(Debug, thiserror::Error)]
pub enum ApiError {
    #[error("Invalid argument: {0}")]
    InvalidArgument(String),
    #[error("Not implemented")]
    NotImplemented,
    #[error("Operation failed: {0}")]
    OperationFailed(String),
}

pub type ApiResult<T> = std::result::Result<T, ApiError>;