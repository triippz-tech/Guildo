import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IGuildApplication, defaultValue } from 'app/shared/model/bot/guild-application.model';

export const ACTION_TYPES = {
  FETCH_GUILDAPPLICATION_LIST: 'guildApplication/FETCH_GUILDAPPLICATION_LIST',
  FETCH_GUILDAPPLICATION: 'guildApplication/FETCH_GUILDAPPLICATION',
  CREATE_GUILDAPPLICATION: 'guildApplication/CREATE_GUILDAPPLICATION',
  UPDATE_GUILDAPPLICATION: 'guildApplication/UPDATE_GUILDAPPLICATION',
  DELETE_GUILDAPPLICATION: 'guildApplication/DELETE_GUILDAPPLICATION',
  SET_BLOB: 'guildApplication/SET_BLOB',
  RESET: 'guildApplication/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildApplication>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GuildApplicationState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildApplicationState = initialState, action): GuildApplicationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDAPPLICATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDAPPLICATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDAPPLICATION):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDAPPLICATION):
    case REQUEST(ACTION_TYPES.DELETE_GUILDAPPLICATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDAPPLICATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDAPPLICATION):
    case FAILURE(ACTION_TYPES.CREATE_GUILDAPPLICATION):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDAPPLICATION):
    case FAILURE(ACTION_TYPES.DELETE_GUILDAPPLICATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDAPPLICATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDAPPLICATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDAPPLICATION):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDAPPLICATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDAPPLICATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/guild-applications';

// Actions

export const getEntities: ICrudGetAllAction<IGuildApplication> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GUILDAPPLICATION_LIST,
  payload: axios.get<IGuildApplication>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGuildApplication> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDAPPLICATION,
    payload: axios.get<IGuildApplication>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildApplication> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDAPPLICATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildApplication> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDAPPLICATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildApplication> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDAPPLICATION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
