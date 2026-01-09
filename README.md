# Aeon Engine — v0.5

**Aeon Engine** is a lightweight render-oriented engine built around an OpenGL backend.
At its current stage, Aeon focuses primarily on providing a clean and extensible **render pipeline**, rather than a full-featured game engine.

The project is designed as a technical foundation for future experiments and game prototypes.

---

## Current Features

Aeon Engine currently supports:

* OpenGL-based rendering backend (LWJGL)
* Mesh rendering
* Texture loading and management
* Shader management
* Directional and Point lights
* Scene system
* Basic 2D text rendering
* Font loading via FreeType

The architecture is component-based and intended to be extended over time.

---

## Project Scope

Aeon Engine is **not** intended to compete with full-scale engines (e.g. Unity, Unreal).
Its main goal is to demonstrate:

* understanding of modern rendering pipelines
* manual resource and state management
* engine architecture design
* separation of rendering, scene logic, and backend-specific code

---

## Requirements

To run a project using Aeon Engine, you must include the **framework dependency**:

```
https://git.bolyuk.org/bolyuk/framework
```

This framework provides the base context, logging, and event infrastructure required by the engine.

---

## Easy Start

Minimal example to initialize and run Aeon Engine:

```java
IContext ctx = BJSInitializer.defaultInit("TEST");

AeonEngine engine = new AeonEngine(ctx, new GLBackend(ctx).get());
engine.initialize("AEON TEST", 800, 500);
engine.loadDefaultResources();
engine.setScene(new YourScene());
engine.start();
```

---

## Notes

* Aeon Engine currently focuses on rendering and scene presentation.
* Physics, UI batching, and advanced optimizations are planned for future versions.
* The engine is actively used as a base for further school and personal projects.

---

## Architecture Overview

Aeon Engine is built around a **layered architecture** with a clear separation between engine logic, rendering backend, and framework services.
The design goal is to keep the core engine **backend-agnostic**, while allowing different rendering implementations.

### High-level structure

```
Application (framework, physic, scenes, etc.)
   ↓
Aeon Engine (engine)
   ↓
Some Abstraction (engine-common)
   ↓
Backend abstraction (renderer-common)
   ↓
OpenGL backend [LWJGL] (gl-core)
```

---

## Core Engine Layer

The **Aeon Engine core** is responsible for:

* Scene management
* Component-based object system (you can void it if you want to)
* Render pipeline orchestration
* Frame lifecycle and update stages
* Resource access abstraction, backend dependent resource fabric
* Event and input dispatching, mouse+keyboard input

Key concepts:

* `Scene` and `SceneObject`
* Component-based design (`Transform`, `Material`, `Model`, etc.)
* Central render loop with clearly defined stages:

  * `Stage.SYSTEM`
  * `INPUT` **<- accessible only by SceneObject**
  * `Stage.BEFORE_SCENE_UPDATE`
  * `UPDATE` **<- accessible only by SceneObject**
  * `Stage.AFTER_SCENE_UPDATE`
  * `Stage.BEFORE_SCENE_RENDER`
  * `RENDER` **<- accessible only by SceneObject**
  * `Stage.AFTER_SCENE_RENDER`

The core does **not** directly depend on OpenGL or any specific graphics API.

---

## Backend Abstraction Layer

The backend layer defines **interfaces** that isolate the engine from low-level graphics and windowing APIs.

Responsibilities:

* Window creation and management
* Input polling
* Viewport and framebuffer handling
* GPU resource creation (textures, shaders, meshes)
* Actual draw calls

This layer allows the engine to theoretically support other backends (e.g. Vulkan) without rewriting the engine logic.

---

## OpenGL Backend (LWJGL)

The current implementation uses **OpenGL via LWJGL**.

Responsibilities:

* OpenGL context initialization
* Shader compilation and binding
* Texture and mesh upload to GPU
* Rendering execution
* Window and input handling (GLFW)
* OpenGL `ResourceFabric`

All OpenGL-specific code is strictly confined to this layer. (but you can override it)

---

## Resource Management

Resources such as:

* textures
* shaders
* meshes
* fonts

are handled via a centralized `ResourceManager`.

Benefits:

* avoids duplicate GPU uploads
* allows resource reuse
* keeps engine logic independent from resource loading details

---

## Scene and Rendering Pipeline

Rendering is performed in the following steps:

1. Collect all renderable objects from the active scene
2. Extract required render data (mesh, material, transform, lights)
3. Build a render frame description
4. Pass the frame to the backend renderer
5. Backend executes draw calls

Lighting is resolved during rendering using:

* one directional light
* multiple point lights

---

## UI (still in progress) and Text Rendering

text rendering uses a **2D orthographic pipeline**:

* Orthographic projection
* Dedicated UI meshes
* Dynamic vertex buffers
* FreeType-based font loading

This system is intentionally simple (not yet) and designed for future batching and instanced rendering.

---

## Design Goals

The architecture prioritizes:

* clarity over feature count
* explicit data flow
* extensibility
* separation of concerns

Aeon Engine is designed as a **technical foundation**, not a finished product.

---

## Known Limitations

At its current stage (v0.5), Aeon Engine has several known limitations.
These limitations are **intentional** and reflect the project’s focus on rendering architecture rather than feature completeness.

### Rendering

* No batching or instanced rendering for UI and text yet
  * Each UI element and glyph is currently rendered with a separate draw call.
* No texture atlases for fonts
  * Each glyph uses its own texture, which limits rendering efficiency.
* Limited material system
  * Materials are basic and primarily support color and texture parameters.
* Lighting model is simple
  * Only basic directional and point lighting is supported.

---

### Engine Systems

* No full physics engine
  * Current physics components are experimental and limited to simple motion logic.
* No collision detection or response
  * Physics interactions are not yet spatially aware.
* No animation system
  * Skeletal animation and keyframe animation are not implemented.
* No ECS-level optimization
  * The component system prioritizes clarity over performance.

---

### UI System

* UI rendering is immediate-mode style
  * No retained UI tree or layout system.
* No automatic layout or scaling
  * UI elements must be positioned manually.
* Limited styling options
  * UI elements currently support only basic shapes and text.

---

### Tooling and Workflow

* No editor or visual tools
  * All scenes and UI are defined in code.
* No serialization for scenes or assets
  * Scene data is not saved or loaded from external formats.
* Minimal error recovery
  * Most runtime errors are logged but not handled gracefully.

---

## Future Work

Planned improvements and extensions include:

### Rendering Improvements

* Instanced rendering for UI elements
* Font atlas generation and batched text rendering
* Extended material system
* Improved lighting models
* Optional post-processing pipeline

---

### Engine Extensions

* Collision detection and basic physics integration
* Improved input system
* Camera controllers and utilities
* More robust scene lifecycle management

---

### UI System Enhancements

* UI batching and render queues
* Layout system (anchors, scaling, constraints)
* Extended UI widgets (buttons, panels, text blocks)
* Event-driven UI interaction system

---

### Architecture & Performance

* Better separation between update and render data
* Reduced per-frame allocations
* Optimized resource lifetime management
* Optional multi-backend support

---

## Long-term Goal

Aeon Engine is intended to serve as a **foundation for future school and personal projects**, where individual systems can be iteratively improved without rewriting the core architecture.

The engine will evolve alongside its use cases rather than aiming for feature parity with existing commercial engines. (but i still want to make a VR game on it)

---

## Why Not Unity?

Unity is a powerful and mature game engine that provides a wide range of features out of the box.
However, for this project, Unity was intentionally **not** used.

The main reason is that the goal of Aeon Engine is **not game production**, but **understanding how an engine works internally**.

### Learning over Convenience

Using Unity would hide most of the critical systems behind abstractions:

* rendering pipeline
* resource management
* GPU state handling
* scene traversal
* input and window management

By building a custom engine, these systems become explicit and understandable rather than implicit and opaque.

---

### Control and Transparency

Aeon Engine provides full control over:

* rendering order and pipeline stages
* OpenGL state changes
* shader usage
* data flow between engine layers

This level of transparency is essential for learning how real-time rendering engines operate at a low level.

---

### Educational Value

Reimplementing core engine systems helps to:

* better understand graphics APIs
* learn engine architecture patterns
* identify performance bottlenecks
* reason about design trade-offs

Even limited or “simpler” implementations provide more insight than using a ready-made solution.

---

### Intentional Reinvention

Reinventing existing solutions (“building bicycles”) is deliberate:

* it exposes hidden complexity
* it highlights why certain abstractions exist
* it builds a deeper technical intuition

The goal is not to outperform existing engines, but to **understand why they are built the way they are**.

---

### Conclusion

Unity is an excellent tool for creating games quickly.
Aeon Engine is a tool for learning how such engines are built.

For this project, learning and architectural exploration were prioritized over productivity.

---