//! Script API definitions (mirror of Java IScriptAPI, IScriptContext, etc.)

use crate::ApiError;

/// Script API interface.
pub trait IScriptAPI {
    fn on(&self, event_name: &str, callback: Box<dyn Fn()>);
    fn cleanup(&self);
    fn log(&self, message: &str);
    fn get_context(&self) -> Box<dyn IScriptContext>;
}

/// Script context interface.
pub trait IScriptContext {
    fn simulate(&self, action: &dyn crate::action::IMouseAction);
    fn get_pixel_color(&self, x: i32, y: i32) -> Result<u32, ApiError>;
    fn show_toast(&self, title: &str, message: &str);
    fn get_app_config(&self) -> Result<Box<dyn crate::config::IConfig>, ApiError>;
}

/// System information interface.
pub trait ISystemInfo {
    fn get_screen_width(&self) -> i32;
    fn get_screen_height(&self) -> i32;
}

/// Screen information interface.
pub trait IScreenInfo {
    fn get_color_at(&self, x: i32, y: i32) -> u32;
}

/// Mouse information interface.
pub trait IMouseInfo {
    fn get_x(&self) -> i32;
    fn get_y(&self) -> i32;
    fn get_button_state(&self) -> u32;
}

/// Macro information interface.
pub trait IMacroInfo {
    fn get_action_count(&self) -> usize;
    fn get_loop_count(&self) -> i32;
}
