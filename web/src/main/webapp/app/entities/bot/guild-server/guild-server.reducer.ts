import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IGuildServer, defaultValue } from 'app/shared/model/bot/guild-server.model';

export const ACTION_TYPES = {
  FETCH_GUILDSERVER_LIST: 'guildServer/FETCH_GUILDSERVER_LIST',
  FETCH_GUILDSERVER: 'guildServer/FETCH_GUILDSERVER',
  CREATE_GUILDSERVER: 'guildServer/CREATE_GUILDSERVER',
  UPDATE_GUILDSERVER: 'guildServer/UPDATE_GUILDSERVER',
  DELETE_GUILDSERVER: 'guildServer/DELETE_GUILDSERVER',
  RESET: 'guildServer/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildServer>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type GuildServerState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildServerState = initialState, action): GuildServerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDSERVER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDSERVER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDSERVER):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDSERVER):
    case REQUEST(ACTION_TYPES.DELETE_GUILDSERVER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDSERVER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDSERVER):
    case FAILURE(ACTION_TYPES.CREATE_GUILDSERVER):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDSERVER):
    case FAILURE(ACTION_TYPES.DELETE_GUILDSERVER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSERVER_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSERVER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDSERVER):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDSERVER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDSERVER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/guild-servers';

// Actions

export const getEntities: ICrudGetAllAction<IGuildServer> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDSERVER_LIST,
    payload: axios.get<IGuildServer>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IGuildServer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDSERVER,
    payload: axios.get<IGuildServer>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildServer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDSERVER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IGuildServer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDSERVER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildServer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDSERVER,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
