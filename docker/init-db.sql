-- Activer TimescaleDB
CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

-- ============================================
-- TABLES MÉTIER
-- ============================================

-- Véhicules
CREATE TABLE IF NOT EXISTS vehicles (
                                        id BIGSERIAL PRIMARY KEY,
                                        immatriculation VARCHAR(20) UNIQUE NOT NULL,
                                        marque VARCHAR(50),
                                        modele VARCHAR(50),
                                        type VARCHAR(30), -- camion, voiture, moto, etc.
                                        capacite_poids DECIMAL(10,2),
                                        capacite_volume DECIMAL(10,2),
                                        conducteur_nom VARCHAR(100),
                                        conducteur_telephone VARCHAR(20),
                                        conducteur_email VARCHAR(100),
                                        groupe VARCHAR(50),
                                        statut VARCHAR(20) DEFAULT 'actif', -- actif, inactif, maintenance
                                        couleur_marqueur VARCHAR(7) DEFAULT '#FF0000',
                                        date_creation TIMESTAMP DEFAULT NOW(),
                                        CONSTRAINT check_statut CHECK (statut IN ('actif', 'inactif', 'maintenance'))
);

CREATE INDEX idx_vehicles_immatriculation ON vehicles(immatriculation);
CREATE INDEX idx_vehicles_statut ON vehicles(statut);

-- Commandes/Livraisons
CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      numero_commande VARCHAR(50) UNIQUE NOT NULL,
                                      client_nom VARCHAR(100) NOT NULL,
                                      client_email VARCHAR(100),
                                      client_telephone VARCHAR(20),
                                      client_adresse VARCHAR(255),
                                      adresse_depart VARCHAR(255),
                                      adresse_arrivee VARCHAR(255),
                                      latitude_depart DECIMAL(10,8),
                                      longitude_depart DECIMAL(11,8),
                                      latitude_arrivee DECIMAL(10,8),
                                      longitude_arrivee DECIMAL(11,8),
                                      vehicule_id BIGINT REFERENCES vehicles(id) ON DELETE SET NULL,
                                      statut VARCHAR(30) DEFAULT 'en_attente',
                                      description_colis TEXT,
                                      poids DECIMAL(10,2),
                                      volume DECIMAL(10,2),
                                      priorite VARCHAR(20) DEFAULT 'normal', -- urgent, normal, basse
                                      date_creation TIMESTAMP DEFAULT NOW(),
                                      date_livraison_souhaitee DATE,
                                      date_livraison_reelle TIMESTAMP,
                                      lien_partage_public UUID DEFAULT gen_random_uuid(),
                                      lien_expire_le TIMESTAMP DEFAULT NOW() + INTERVAL '7 days',
                                      CONSTRAINT check_statut_order CHECK (statut IN ('en_attente', 'assignee', 'en_cours', 'livree', 'exception'))
);

CREATE INDEX idx_orders_numero ON orders(numero_commande);
CREATE INDEX idx_orders_vehicule_id ON orders(vehicule_id);
CREATE INDEX idx_orders_statut ON orders(statut);
CREATE INDEX idx_orders_lien_partage ON orders(lien_partage_public);

-- ============================================
-- TABLES SÉRIES TEMPORELLES (TimescaleDB)
-- ============================================

-- Positions GPS
CREATE TABLE IF NOT EXISTS gps_positions (
                                             time TIMESTAMP NOT NULL,
                                             vehicule_id BIGINT NOT NULL,
                                             latitude DECIMAL(10,8) NOT NULL,
                                             longitude DECIMAL(11,8) NOT NULL,
                                             vitesse DECIMAL(5,2),
                                             orientation DECIMAL(6,2),
                                             altitude DECIMAL(10,2),
                                             source VARCHAR(20) DEFAULT 'simule', -- 'simule' ou 'reel'
                                             commande_id BIGINT,
                                             CONSTRAINT fk_vehicule FOREIGN KEY (vehicule_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

SELECT create_hypertable('gps_positions', 'time', if_not_exists => TRUE);
CREATE INDEX idx_positions_vehicule_time ON gps_positions (vehicule_id, time DESC);
CREATE INDEX idx_positions_commande ON gps_positions (commande_id);

-- Événements
CREATE TABLE IF NOT EXISTS events (
                                      time TIMESTAMP NOT NULL,
                                      vehicule_id BIGINT NOT NULL,
                                      type VARCHAR(50) NOT NULL, -- 'depassement_vitesse', 'entree_zone', 'sortie_zone', 'arret', 'demarrage', etc.
                                      description TEXT,
                                      latitude DECIMAL(10,8),
                                      longitude DECIMAL(11,8),
                                      severity VARCHAR(20) DEFAULT 'info', -- 'info', 'warning', 'critical'
                                      details JSONB,
                                      CONSTRAINT fk_vehicule_event FOREIGN KEY (vehicule_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

SELECT create_hypertable('events', 'time', if_not_exists => TRUE);
CREATE INDEX idx_events_vehicule_time ON events (vehicule_id, time DESC);
CREATE INDEX idx_events_type ON events (type);

-- Alertes
CREATE TABLE IF NOT EXISTS alerts (
                                      id BIGSERIAL PRIMARY KEY,
                                      vehicule_id BIGINT NOT NULL REFERENCES vehicles(id) ON DELETE CASCADE,
                                      type VARCHAR(50) NOT NULL, -- 'depassement_vitesse', 'entree_zone', 'sortie_zone', 'perte_signal', 'batterie_faible'
                                      seuil VARCHAR(255),
                                      statut VARCHAR(20) DEFAULT 'active', -- 'active', 'resolue', 'ignoree'
                                      message TEXT,
                                      date_creation TIMESTAMP DEFAULT NOW(),
                                      date_resolution TIMESTAMP,
                                      lue BOOLEAN DEFAULT FALSE,
                                      CONSTRAINT check_statut_alerte CHECK (statut IN ('active', 'resolue', 'ignoree'))
);

CREATE INDEX idx_alerts_vehicule ON alerts(vehicule_id);
CREATE INDEX idx_alerts_statut ON alerts(statut);
CREATE INDEX idx_alerts_lue ON alerts(lue);

-- ============================================
-- DONNÉES DE TEST
-- ============================================

-- Insérer des véhicules de test
INSERT INTO vehicles (immatriculation, marque, modele, type, capacite_poids, capacite_volume, conducteur_nom, conducteur_telephone, groupe, statut, couleur_marqueur)
VALUES
    ('MAR-001', 'Mercedes', 'Sprinter', 'camion', 2500.00, 12.00, 'Ahmed Ben Salah', '+216 95 123 456', 'groupe_a', 'actif', '#FF0000'),
    ('REN-002', 'Renault', 'Master', 'camion', 2000.00, 10.00, 'Mohamed Khoudi', '+216 96 234 567', 'groupe_a', 'actif', '#00FF00'),
    ('PSA-003', 'Peugeot', 'Boxer', 'camion', 1500.00, 8.00, 'Farah Dammak', '+216 97 345 678', 'groupe_b', 'actif', '#0000FF'),
    ('VOL-004', 'Volvo', 'FH16', 'camion', 3500.00, 18.00, 'Ali Romdhani', '+216 98 456 789', 'groupe_b', 'actif', '#FFFF00');

-- Insérer des commandes de test
INSERT INTO orders (numero_commande, client_nom, client_email, client_telephone, client_adresse, adresse_depart, adresse_arrivee, latitude_depart, longitude_depart, latitude_arrivee, longitude_arrivee, vehicule_id, statut, description_colis, poids, priorite)
VALUES
    ('CMD-2025-0001', 'Entreprise ABC', 'contact@abc.tn', '71 123 456', 'Tunis', 'Tunis Port', 'Sfax Warehouse', 36.8065, 10.1815, 34.7405, 10.7673, 1, 'en_cours', 'Électronique fragile', 150.50, 'urgent'),
    ('CMD-2025-0002', 'Client XYZ', 'xyz@client.tn', '72 234 567', 'Ariana', 'Ariana Hub', 'Sousse Shop', 36.8620, 10.1957, 35.8256, 10.6369, 2, 'assignee', 'Pièces automobiles', 300.00, 'normal'),
    ('CMD-2025-0003', 'Shop Retail', 'info@retail.tn', '73 345 678', 'La Marsa', 'Tunis Center', 'Bizerte Port', 36.8065, 10.1815, 37.2741, 9.8739, 3, 'en_attente', 'Textiles', 500.75, 'normal');

-- Insérer quelques positions GPS de test (derniers 30 minutes)
INSERT INTO gps_positions (time, vehicule_id, latitude, longitude, vitesse, orientation, altitude, source, commande_id)
VALUES
    (NOW() - INTERVAL '30 minutes', 1, 36.8065, 10.1815, 0.0, 0.0, 50.0, 'simule', 1),
    (NOW() - INTERVAL '20 minutes', 1, 36.8150, 10.1900, 45.5, 85.0, 52.0, 'simule', 1),
    (NOW() - INTERVAL '10 minutes', 1, 36.8250, 10.2050, 55.0, 90.0, 54.0, 'simule', 1),
    (NOW(), 1, 36.8350, 10.2150, 48.5, 92.0, 56.0, 'simule', 1),

    (NOW() - INTERVAL '25 minutes', 2, 36.8620, 10.1957, 0.0, 0.0, 48.0, 'simule', 2),
    (NOW() - INTERVAL '15 minutes', 2, 36.8550, 10.2100, 52.0, 95.0, 50.0, 'simule', 2),
    (NOW() - INTERVAL '5 minutes', 2, 36.8500, 10.2300, 60.0, 100.0, 52.0, 'simule', 2),
    (NOW(), 2, 36.8450, 10.2450, 55.0, 102.0, 54.0, 'simule', 2);

-- Insérer quelques événements de test
INSERT INTO events (time, vehicule_id, type, description, latitude, longitude, severity)
VALUES
    (NOW() - INTERVAL '10 minutes', 1, 'demarrage', 'Véhicule démarré', 36.8065, 10.1815, 'info'),
    (NOW() - INTERVAL '8 minutes', 1, 'depassement_vitesse', 'Vitesse 85 km/h zone 50', 36.8150, 10.1900, 'warning'),
    (NOW() - INTERVAL '5 minutes', 2, 'demarrage', 'Véhicule démarré', 36.8620, 10.1957, 'info');

-- Insérer une alerte de test
INSERT INTO alerts (vehicule_id, type, seuil, message, statut)
VALUES
    (1, 'depassement_vitesse', '50', 'Vitesse max 50 km/h dépassée', 'active');

COMMIT;