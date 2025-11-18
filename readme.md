### Problem
- Design and implement a simulated Car Rental system using object-oriented principles.
- The system should allow reservation of a car of a given type at a desired date and time for a given number of days.
- There are 3 types of cars (sedan, SUV, and van).
- The number of cars of each type is limited.

### Solution
- The implementation is developed in Java and follows OOP and SOLID principles.
- The design uses generics, which allows introducing new types of reservables simply by extending the Reservable class and updating the ReservableEnum accordingly.
- The system is concurrency-safe: all cars in the inventory can be checked and reserved in parallel as long as they are available for the requested time window; the only restriction is that a particular car instance cannot be reserved or cancelled concurrently.
- Reservation cancellation functionality has been added to improve testability.

### Design
The design is based on the following core components:
- ConcurrentRegistry – provides reserve() and cancel(reservationId) operations and maintains the inventory.
- TimeTable – a calendar-like structure that works with TimeSlot objects and tracks all reservations.
- TimeSlot – a discrete atomic time unit that contains and encapsulates all reservations associated with it.
- Reservable – an abstract representation of items such as Sedan, SUV, or Van that implement lockable and hashable behavior.

### Room for customization
- Availability-by-type queries can be exposed through the CarRentalApplication API.
- The reservation system can be extended to other types of reservables (e.g., bicycles, accommodations, equipment, etc.).