
USE esports_db;

-- Index audit for the eSports League Management System.
-- Existing primary keys, foreign keys, and unique constraints
-- already provide indexes for many common lookup operations.
--
-- EXPLAIN audit:
-- The matches query used a table scan (type ALL).
-- With only two match rows, this is reasonable.
--
-- Decision:
-- Do not add indexes solely to eliminate this table scan.
-- Re-evaluate indexes as the dataset and query workload grow.
--
-- Candidate indexes to evaluate in the future:
-- 1. matches(status, tournament_id)
-- 2. team_rosters(team_id, start_date, end_date)
-- 3. player_transfers(player_id, transfer_date)
--
-- Use EXPLAIN and realistic data volumes before creating them.

SHOW INDEX FROM matches;
SHOW INDEX FROM team_rosters;
SHOW INDEX FROM player_transfers;
