//! Event API definitions.

/// Cancellable event trait.
pub trait Cancellable {
    fn is_cancelled(&self) -> bool;
    fn set_cancelled(&mut self, cancelled: bool);
}

/// Event priority.
#[derive(Debug, Clone, Copy, PartialEq, Eq, Hash, PartialOrd, Ord)]
pub enum EventPriority {
    Lowest,
    Low,
    Normal,
    High,
    Highest,
    Monitor,
}

/// Event handler trait.
pub trait EventHandler<T> {
    fn handle(&self, event: &mut T);
}

/// Event listener marker.
pub trait Listener {}

/// Event type.
pub struct Event {
    // Placeholder
}
