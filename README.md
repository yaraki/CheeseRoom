# CheeseRoom

This is a sample of Room.

The project has 5 modules.

- app-kotlin: CheeseRoom written in Kotlin
- app-java: CheeseRoom written in Sample
- app-start: CheeseRoom codelab in Kotlin
- common: This module has data such as cheese images
- playground: Demonstrates some techniques of Room

## Structure of the App

### UI

- MainActivity
  - CheeseListFragment: shows the list of cheeses
    - CheeseListViewModel
  - CheeseDetailFragment: shows the detail of a selected cheese. User can mark it favorite.
    - CheeseDetailViewModel

### Data Source

See
[Guide to App Architecture](https://developer.android.com/topic/libraries/architecture/guide.html)
for the overall design of this layer.

- CheeseRepository
  - CheeseApi : Web API (It actually returns a constant data, but just pretend that it is a
    synchronous network call.)
  - CheeseDatabase : The main database

### Database

The database has only 1 table.

- Cheese: table
  - id: primary key, integer
  - name: text
  - favorite: boolean

## Codelab

1. Choose 'app-start' from the drop-down menu of the configurations.
2. Read MainActivity.kt, CheeseListFragment.kt, and CheeseDetailFragment.kt.
3. Finish TODOs in Cheese.kt.
4. Finish TODOs in CheeseDatabase.kt.
5. Finish TODOs in CheeseDao.kt.
6. Finish TODOs in CheeseRepository.kt.
7. Finish TODOs in CheeseListViewModel.kt and CheeseDetailViewModel.kt.
8. Run CheeseDatabaseTest

## What Next?

[GithubBrowserSample](https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample)

