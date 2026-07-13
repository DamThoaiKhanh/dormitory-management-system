# Dormitory Management System — UI Specification

## 1. Overview

This document describes the UI for a dormitory management system built with:

- **Frontend:** Vue 3
- **Backend:** Spring Boot
- **Database:** PostgreSQL
- **Primary users:** Dormitory administrators and management staff

Focuses on basic CRUD operations for:

- Buildings
- Rooms
- Students
- Management staff

The interface should use a simple, clean admin-dashboard style.

---

## 2. Main Application Layout

```text
┌──────────────────────────────────────────────────────────────────┐
│ Dormitory Manager                         🔔   Admin ▼           │
├───────────────┬──────────────────────────────────────────────────┤
│ Dashboard     │                                                  │
│ Buildings     │                 Main content                     │
│ Rooms         │                                                  │
│ Students      │                                                  │
│ Staff         │                                                  │
│ Settings      │                                                  │
└───────────────┴──────────────────────────────────────────────────┘
```

### Layout behavior

- The sidebar remains fixed on desktop.
- The sidebar collapses into a menu button on mobile.
- The top bar contains:
  - Application name
  - Notification icon
  - Current user menu
- The main content area displays the selected page.
- The active sidebar item should be visually highlighted.

---

## 3. Visual Style

Use a simple admin interface with:

- Light-gray application background
- White content cards
- One primary accent color
- Clear page titles
- Consistent spacing
- Rounded corners
- Subtle shadows
- Tables for desktop
- Card-based lists for mobile
- Clear status badges

### Suggested status labels

| Status    | Meaning                       |
| --------- | ----------------------------- |
| Active    | Record is available for use   |
| Inactive  | Record is disabled            |
| Available | Room has free beds            |
| Full      | Room has reached capacity     |
| Empty     | Room has no assigned students |

---

# 4. Dashboard

```text
┌──────────────────────────────────────────────────────────────┐
│ Dashboard                                                    │
│ Overview of the dormitory system                             │
├──────────────┬──────────────┬──────────────┬─────────────────┤
│ Buildings    │ Rooms        │ Students     │ Available beds  │
│     4        │     80       │     245      │       35        │
└──────────────┴──────────────┴──────────────┴─────────────────┘

┌──────────────────────────────┐ ┌─────────────────────────────┐
│ Room occupancy               │ │ Recent students             │
│                              │ │                             │
│ Building A     85%           │ │ Nguyễn Văn A     A-101     │
│ Building B     73%           │ │ Trần Thị B        B-204     │
│ Building C     92%           │ │ Lê Văn C          A-305     │
└──────────────────────────────┘ └─────────────────────────────┘
```

### Dashboard cards

- Total buildings
- Total rooms
- Total students
- Available beds

### Additional dashboard content

- Occupancy percentage by building
- Recently added students
- Recently added rooms
- Number of inactive buildings or rooms

For Phase 1, occupancy can be represented with progress bars instead of advanced charts.

---

# 5. Buildings Page

## 5.1 Building List

```text
┌──────────────────────────────────────────────────────────────┐
│ Buildings                                      + Add building│
├──────────────────────────────────────────────────────────────┤
│ Search buildings...              Status: All ▼               │
├──────────┬──────────────────┬────────┬──────────┬─────────────┤
│ Code     │ Building name    │ Floors │ Status   │ Actions     │
├──────────┼──────────────────┼────────┼──────────┼─────────────┤
│ A        │ Building A       │ 5      │ Active   │ View Edit ⋮ │
│ B        │ Building B       │ 4      │ Active   │ View Edit ⋮ │
│ C        │ Building C       │ 3      │ Inactive │ View Edit ⋮ │
└──────────┴──────────────────┴────────┴──────────┴─────────────┘
```

### Page features

- Search by building code or building name
- Filter by status
- Add building button
- View building
- Edit building
- Delete or deactivate building
- Pagination when the list grows

## 5.2 Building Form

```text
┌──────────────────────── Add building ────────────────────────┐
│ Building code     [ A                              ]          │
│ Building name     [ Building A                     ]          │
│ Address           [                                ]          │
│ Number of floors  [ 5                              ]          │
│ Description       [                                ]          │
│ Status            [ Active ▼                       ]          │
│                                                              │
│                              Cancel      Save building       │
└──────────────────────────────────────────────────────────────┘
```

### Building form fields

| Field            | Required | Notes                 |
| ---------------- | -------: | --------------------- |
| Building code    |      Yes | Must be unique        |
| Building name    |      Yes | Display name          |
| Address          |       No | Full building address |
| Number of floors |       No | Positive integer      |
| Description      |       No | Long text             |
| Status           |      Yes | Active or inactive    |

The form may be displayed as a modal, side drawer, or dedicated page.

---

# 6. Rooms Page

## 6.1 Room List

```text
┌──────────────────────────────────────────────────────────────┐
│ Rooms                                              + Add room│
├──────────────────────────────────────────────────────────────┤
│ Search room...   Building: All ▼   Status: All ▼             │
├────────┬──────────┬────────────┬──────────┬────────┬──────────┤
│ Room   │ Building │ Occupancy  │ Capacity │ Rent   │ Actions  │
├────────┼──────────┼────────────┼──────────┼────────┼──────────┤
│ 101    │ A        │ 3 / 4      │ 4        │ $80    │ Edit ⋮   │
│ 102    │ A        │ 4 / 4      │ 4        │ $80    │ Edit ⋮   │
│ 201    │ B        │ 1 / 6      │ 6        │ $100   │ Edit ⋮   │
└────────┴──────────┴────────────┴──────────┴────────┴──────────┘
```

### Page features

- Search by room number
- Filter by building
- Filter by status
- Show current occupancy
- Show maximum capacity
- Show monthly rent
- Add room
- Edit room
- View room details

### Occupancy labels

```text
1 / 4   Available
4 / 4   Full
0 / 4   Empty
```

## 6.2 Room Form

```text
┌────────────────────────── Add room ──────────────────────────┐
│ Room number       [ 101                            ]          │
│ Building          [ Building A ▼                   ]          │
│ Monthly rent      [ 80                             ]          │
│ Maximum capacity  [ 4                              ]          │
│ Status            [ Active ▼                       ]          │
│                                                              │
│                                   Cancel     Save room       │
└──────────────────────────────────────────────────────────────┘
```

### Room form fields

| Field            | Required | Notes                                       |
| ---------------- | -------: | ------------------------------------------- |
| Room number      |      Yes | Must be unique within the selected building |
| Building         |      Yes | Selected from existing buildings            |
| Monthly rent     |       No | Must be zero or greater                     |
| Maximum capacity |      Yes | Must be a positive integer                  |
| Status           |      Yes | Active or inactive                          |

## 6.3 Room Detail Page

```text
┌──────────────────────────────────────────────────────────────┐
│ ← Rooms       Room A-101                       Edit room      │
├──────────────────────────────────────────────────────────────┤
│ Building: A              Capacity: 4                         │
│ Monthly rent: $80        Status: Active                      │
│ Occupied beds: 3         Available beds: 1                   │
├──────────────────────────────────────────────────────────────┤
│ Current students                                             │
│                                                              │
│ Nguyễn Văn A     SV001     Computer Science     Remove       │
│ Trần Thị B       SV002     Business             Remove       │
│ Lê Văn C         SV003     Engineering          Remove       │
└──────────────────────────────────────────────────────────────┘
```

---

# 7. Students Page

## 7.1 Student List

```text
┌──────────────────────────────────────────────────────────────┐
│ Students                                         + Add student│
├──────────────────────────────────────────────────────────────┤
│ Search name or ID...    Building: All ▼   Room: All ▼        │
├───────────────┬──────────┬───────────┬────────┬───────────────┤
│ Student       │ ID       │ Class     │ Room   │ Actions       │
├───────────────┼──────────┼───────────┼────────┼───────────────┤
│ Nguyễn Văn A  │ SV001    │ CNTT-01   │ A-101  │ View Edit ⋮   │
│ Trần Thị B    │ SV002    │ QTKD-02   │ A-101  │ View Edit ⋮   │
│ Lê Văn C      │ SV003    │ CNTT-01   │ —      │ View Edit ⋮   │
└───────────────┴──────────┴───────────┴────────┴───────────────┘
```

### Page features

- Search by student name or student ID
- Filter by building
- Filter by room
- Add student
- View student details
- Edit student
- Delete student
- Show unassigned students with `—` in the room column

---

## 7.2 Student Form

The Student Form should be a dedicated page or a large modal because it contains many fields.

```text
┌──────────────────────────────────────────────────────────────┐
│ ← Students                         Add student                │
│ Create a new student profile                                 │
├──────────────────────────────────────────────────────────────┤
│ Personal information                                         │
│                                                              │
│ Full name *            [ Nguyễn Văn A                   ]     │
│ Gender                 [ Male ▼                         ]     │
│ Date of birth          [ 12/02/2004                    ]     │
│ Citizen ID             [ 012345678901                  ]     │
│ Phone number           [ 0912345678                    ]     │
│ Address                [                                 ]     │
├──────────────────────────────────────────────────────────────┤
│ Academic information                                         │
│                                                              │
│ Student ID *           [ SV001                         ]     │
│ Class                  [ CNTT-01                       ]     │
│ Major                  [ Computer Science              ]     │
│ Hometown               [ Hanoi                         ]     │
├──────────────────────────────────────────────────────────────┤
│ Room assignment                                              │
│                                                              │
│ Building               [ Building A ▼                  ]     │
│ Room                   [ Room 101 ▼                     ]     │
│                                                              │
│ Room 101: 3 of 4 beds occupied                               │
├──────────────────────────────────────────────────────────────┤
│                                  Cancel     Save student      │
└──────────────────────────────────────────────────────────────┘
```

### Student form sections

#### A. Personal information

| Field         | Required | Notes                                  |
| ------------- | -------: | -------------------------------------- |
| Full name     |      Yes | Student's legal or preferred full name |
| Gender        |       No | Select value from a dropdown           |
| Date of birth |       No | Use a date picker                      |
| Citizen ID    |       No | Must be unique when provided           |
| Phone number  |       No | Validate phone format                  |
| Address       |       No | Multi-line input                       |

#### B. Academic information

| Field      | Required | Notes                     |
| ---------- | -------: | ------------------------- |
| Student ID |      Yes | Must be unique            |
| Class      |       No | Example: CNTT-01          |
| Major      |       No | Example: Computer Science |
| Hometown   |       No | Student's place of origin |

#### C. Room assignment

| Field    | Required | Notes                                    |
| -------- | -------: | ---------------------------------------- |
| Building |       No | Filters the room dropdown                |
| Room     |       No | Can remain empty for unassigned students |

### Student form behavior

- The Room dropdown remains disabled until a building is selected.
- Only rooms in the selected building are displayed.
- Full or inactive rooms should not be selectable.
- The current occupancy should appear below the room selector.
- Room assignment is optional in Phase 1.
- Selecting no room creates an unassigned student.
- Changing the building should clear the selected room.
- The Save button should be disabled while the request is processing.

### Example validation states

```text
Student ID *
[ SV001                           ]
This student ID already exists.

Citizen ID
[ 012345678901                    ]
This citizen ID is already registered.

Phone number
[ 123                             ]
Enter a valid phone number.

Room
[ Room 102 ▼                      ]
This room has reached maximum capacity.
```

### Form actions

- **Cancel:** Return to the student list without saving
- **Save student:** Validate and create the student
- After saving successfully:
  - Show a success message
  - Redirect to the student detail page or student list

### Edit mode

The same form can be reused for editing.

```text
┌──────────────────────────────────────────────────────────────┐
│ ← Student details                  Edit student               │
│ Update student information                                  │
├──────────────────────────────────────────────────────────────┤
│ Existing student values are pre-filled                       │
│                                                              │
│                                  Cancel     Save changes      │
└──────────────────────────────────────────────────────────────┘
```

In edit mode:

- Existing values are pre-filled.
- Student ID uniqueness should ignore the current student.
- Room assignment changes should update the current room.
- A confirmation dialog may appear before removing the student from a room.

---

## 7.3 Student Detail Page

```text
┌──────────────────────────────────────────────────────────────┐
│ ← Students     Nguyễn Văn A                   Edit student   │
├──────────────────────────────────────────────────────────────┤
│ [Avatar]  Nguyễn Văn A                                      │
│           Student ID: SV001                                 │
│           Room: A-101                                       │
├──────────────────────────────────────────────────────────────┤
│ Personal information                                        │
│                                                              │
│ Date of birth: 12/02/2004                                   │
│ Gender: Male                                                 │
│ Phone: 0912345678                                            │
│ Citizen ID: 012345678901                                     │
│ Address: Hanoi                                               │
├──────────────────────────────────────────────────────────────┤
│ Academic information                                        │
│                                                              │
│ Class: CNTT-01                                               │
│ Major: Computer Science                                      │
│ Hometown: Hanoi                                              │
└──────────────────────────────────────────────────────────────┘
```

---

# 8. Management Staff Page

## 8.1 Staff List

```text
┌──────────────────────────────────────────────────────────────┐
│ Management staff                                + Add employee│
├──────────────────────────────────────────────────────────────┤
│ Search employee...        Assigned building: All ▼           │
├────────────────┬──────────────┬────────────┬──────────────────┤
│ Employee       │ Employee ID  │ Building   │ Actions          │
├────────────────┼──────────────┼────────────┼──────────────────┤
│ Phạm Văn D     │ NV001        │ A          │ View Edit ⋮      │
│ Hoàng Thị E    │ NV002        │ B          │ View Edit ⋮      │
└────────────────┴──────────────┴────────────┴──────────────────┘
```

### Page features

- Search by employee name or employee ID
- Filter by assigned building
- Add employee
- View employee
- Edit employee
- Delete employee

## 8.2 Staff Form

```text
┌──────────────────────── Add employee ────────────────────────┐
│ Full name *          [                                ]       │
│ Gender               [ Select ▼                       ]       │
│ Citizen ID           [                                ]       │
│ Phone number         [                                ]       │
│ Date of birth        [                                ]       │
│ Address              [                                ]       │
│ Employee ID *        [                                ]       │
│ Assigned building    [ Building A ▼                   ]       │
│                                                              │
│                              Cancel      Save employee       │
└──────────────────────────────────────────────────────────────┘
```

---

# 9. Reusable Vue Components

```text
src/
├── components/
│   ├── layout/
│   │   ├── AppSidebar.vue
│   │   ├── AppHeader.vue
│   │   └── PageContainer.vue
│   ├── common/
│   │   ├── DataTable.vue
│   │   ├── SearchInput.vue
│   │   ├── StatusBadge.vue
│   │   ├── ConfirmDialog.vue
│   │   ├── FormField.vue
│   │   ├── EmptyState.vue
│   │   └── Pagination.vue
│   ├── building/
│   │   └── BuildingForm.vue
│   ├── room/
│   │   ├── RoomForm.vue
│   │   └── RoomOccupancyBadge.vue
│   ├── student/
│   │   ├── StudentForm.vue
│   │   ├── StudentProfile.vue
│   │   └── StudentTable.vue
│   └── staff/
│       └── StaffForm.vue
├── views/
│   ├── DashboardView.vue
│   ├── BuildingListView.vue
│   ├── BuildingDetailView.vue
│   ├── RoomListView.vue
│   ├── RoomDetailView.vue
│   ├── StudentListView.vue
│   ├── StudentFormView.vue
│   ├── StudentDetailView.vue
│   ├── StaffListView.vue
│   └── StaffDetailView.vue
```

---

# 10. Suggested Routes

```text
/
├── /dashboard
├── /buildings
├── /buildings/new
├── /buildings/:code
├── /buildings/:code/edit
├── /rooms
├── /rooms/new
├── /rooms/:id
├── /rooms/:id/edit
├── /students
├── /students/new
├── /students/:id
├── /students/:id/edit
├── /staff
├── /staff/new
├── /staff/:id
└── /staff/:id/edit
```

---

# 11. Responsive Behavior

## Desktop

- Fixed sidebar
- Full-width tables
- Forms use two columns where appropriate
- Detail pages use card sections

## Tablet

- Collapsible sidebar
- Tables may allow horizontal scrolling
- Forms use one or two columns depending on width

## Mobile

- Sidebar becomes a drawer
- Tables become stacked cards
- Form fields use one column
- Action buttons remain easy to reach
- Primary action may be fixed at the bottom of long forms

---

# 12. Recommended Phase 1 Screens

The first UI prototype should include:

1. Dashboard
2. Building list
3. Building form
4. Room list
5. Room form
6. Room detail
7. Student list
8. Student form
9. Student detail
10. Staff list
11. Staff form

These screens are enough to establish the application's visual system and support all Phase 1 CRUD operations.
