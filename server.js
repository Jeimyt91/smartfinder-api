require('dotenv').config();
const express = require('express');
const cors = require('cors');
const { Pool } = require('pg');

const app = express();
app.use(cors());
app.use(express.json());

const pool = new Pool({
  host: process.env.PGHOST,
  user: process.env.PGUSER,
  password: process.env.PGPASSWORD,
  database: process.env.PGDATABASE,
  port: process.env.PGPORT || 5432,
});

app.get('/api/health', async (req, res) => {
  try {
    await pool.query('SELECT 1');
    res.json({ ok: true });
  } catch (e) {
    res.status(500).json({ ok: false, error: e.message });
  }
});

/* ======================
   MARCAS
====================== */
app.get('/api/brands', async (req, res) => {
  try {
    const { rows } = await pool.query('SELECT id, nombre FROM marcas ORDER BY nombre ASC');
    res.json(rows);
  } catch (e) { res.status(500).send(e.message); }
});

app.post('/api/brands', async (req, res) => {
  try {
    const { nombre } = req.body;
    const { rows } = await pool.query(
      'INSERT INTO marcas (nombre) VALUES ($1) ON CONFLICT (nombre) DO NOTHING RETURNING id, nombre',
      [nombre]
    );
    if (!rows.length) return res.status(409).send('La marca ya existe');
    res.json(rows[0]);
  } catch (e) { res.status(500).send(e.message); }
});

/* ======================
   DISPOSITIVOS
====================== */
// Listado con filtros q, brandId, type, sort
app.get('/api/dispositivos', async (req, res) => {
  try {
    const { q = '', brandId = '', type = '', sort = 'release_desc' } = req.query;
    const parts = [];
    const params = [];
    let idx = 1;

    if (q) {
      parts.push(`(LOWER(d.nombre) LIKE $${idx} OR LOWER(m.nombre) LIKE $${idx} OR LOWER(COALESCE(d.descripcion, '')) LIKE $${idx})`);
      params.push(`%${q.toLowerCase()}%`); idx++;
    }
    if (brandId) {
      parts.push(`d.marca_id = $${idx}`); params.push(brandId); idx++;
    }
    if (type) {
      parts.push(`LOWER(d.tipo) = $${idx}`); params.push(type.toLowerCase()); idx++;
    }

    let orderBy = 'd.fecha_lanzamiento DESC';
    if (sort === 'release_asc') orderBy = 'd.fecha_lanzamiento ASC';
    else if (sort === 'price_asc') orderBy = 'd.precio ASC NULLS LAST';
    else if (sort === 'price_desc') orderBy = 'd.precio DESC NULLS LAST';

    const where = parts.length ? 'WHERE ' + parts.join(' AND ') : '';
    const sql = `
      SELECT d.id, d.nombre, d.tipo, d.precio, d.fecha_lanzamiento, d.descripcion, d.marca_id,
             m.nombre AS marca
      FROM dispositivos d
      JOIN marcas m ON m.id = d.marca_id
      ${where}
      ORDER BY ${orderBy}
    `;
    const { rows } = await pool.query(sql, params);
    res.json(rows);
  } catch (e) { res.status(500).send(e.message); }
});

// Detalle
app.get('/api/dispositivos/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const { rows } = await pool.query(`
      SELECT d.id, d.nombre, d.tipo, d.precio, d.fecha_lanzamiento, d.descripcion,
             d.marca_id, m.nombre AS marca
      FROM dispositivos d
      JOIN marcas m ON m.id = d.marca_id
      WHERE d.id = $1
    `, [id]);
    if (!rows.length) return res.status(404).send('No encontrado');
    res.json(rows[0]);
  } catch (e) { res.status(500).send(e.message); }
});

// Crear
app.post('/api/dispositivos', async (req, res) => {
  try {
    const { marca_id, nombre, tipo, fecha_lanzamiento, precio, descripcion } = req.body;
    const { rows } = await pool.query(`
      INSERT INTO dispositivos(marca_id, nombre, tipo, fecha_lanzamiento, precio, descripcion)
      VALUES ($1,$2,$3,$4,$5,$6)
      RETURNING id, nombre
    `, [marca_id, nombre, tipo, fecha_lanzamiento || null, precio || null, descripcion || null]);
    res.json(rows[0]);
  } catch (e) { res.status(500).send(e.message); }
});

// Actualizar
app.put('/api/dispositivos/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const { marca_id, nombre, tipo, fecha_lanzamiento, precio, descripcion } = req.body;
    await pool.query(`
      UPDATE dispositivos
      SET marca_id=$1, nombre=$2, tipo=$3, fecha_lanzamiento=$4, precio=$5, descripcion=$6
      WHERE id=$7
    `, [marca_id, nombre, tipo, fecha_lanzamiento || null, precio || null, descripcion || null, id]);
    res.json({ ok:true });
  } catch (e) { res.status(500).send(e.message); }
});

// Eliminar
app.delete('/api/dispositivos/:id', async (req, res) => {
  try {
    const { id } = req.params;
    await pool.query('DELETE FROM dispositivos WHERE id=$1', [id]);
    res.json({ ok:true });
  } catch (e) { res.status(500).send(e.message); }
});

/* ======================
   COMENTARIOS
====================== */
app.get('/api/dispositivos/:id/comentarios', async (req, res) => {
  try {
    const { id } = req.params;
    const { rows } = await pool.query(`
      SELECT id, autor, calificacion, texto, fecha
      FROM comentarios
      WHERE dispositivo_id = $1
      ORDER BY fecha DESC
    `, [id]);
    res.json(rows);
  } catch (e) { res.status(500).send(e.message); }
});

app.post('/api/dispositivos/:id/comentarios', async (req, res) => {
  try {
    const { id } = req.params;
    const { autor, calificacion, texto } = req.body;
    await pool.query(`
      INSERT INTO comentarios(dispositivo_id, autor, calificacion, texto)
      VALUES ($1,$2,$3,$4)
    `, [id, autor || 'Anónimo', calificacion || 5, texto]);
    res.json({ ok:true });
  } catch (e) { res.status(500).send(e.message); }
});

app.listen(process.env.PORT || 3000, () => {
  console.log(`✅ API en http://localhost:${process.env.PORT || 3000}/api`);
});
