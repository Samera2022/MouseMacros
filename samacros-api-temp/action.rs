//! Mouse action API definitions.

/// Mouse action interface.
pub trait IMouseAction {
    fn get_x(&self) -> i32;
    fn get_y(&self) -> i32;
    fn get_button(&self) -> i32;
    fn get_action_type(&self) -> ActionType;
    fn get_delay(&self) -> u64;
}

/// Action type enum.
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum ActionType {
    Press,
    Release,
    Move,
    Wheel,
    KeyPress,
    KeyRelease,
    Delay,
}
